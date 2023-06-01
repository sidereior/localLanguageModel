use std::collections::HashMap;
use rand::seq::SliceRandom;
use rand::thread_rng;
use actix_web::{web, App, HttpResponse, HttpServer, Responder};

struct NGramLanguageModel {
    n: usize,
    ngrams: HashMap<Vec<String>, Vec<String>>,
}

impl NGramLanguageModel {
    fn new(n: usize) -> Self {
        NGramLanguageModel {
            n,
            ngrams: HashMap::new(),
        }
    }
    
    fn train(&mut self, text: &str) {
        let words: Vec<String> = text.split_whitespace().map(|w| w.to_string()).collect();
        
        for i in 0..(words.len() - self.n) {
            let ngram = words[i..(i+self.n)].to_vec();
            let next_word = words[i+self.n].clone();
            
            self.ngrams.entry(ngram).or_insert(Vec::new()).push(next_word);
        }
    }
    
    fn generate(&self, prompt: &str, length: usize) -> String {
        let mut output: Vec<String> = prompt.split_whitespace().map(|w| w.to_string()).collect();
        
        let mut rng = thread_rng();
        for _ in 0..length {
            let ngram = output[(output.len() - self.n)..].to_vec();
            
            if let Some(next_words) = self.ngrams.get(&ngram) {
                let next_word = next_words.choose(&mut rng).unwrap().clone();
                output.push(next_word);
            } else {
                break;
            }
        }
        
        output.join(" ")
    }
}

async fn index() -> impl Responder {
    HttpResponse::Ok().body(
        r#"
        <html>
        <head><title>Prompt Generator</title></head>
        <body>
            <h1>Prompt Generator</h1>
            <form action="/generate" method="post">
                <input type="text" name="prompt" placeholder="Enter your prompt" required />
                <button type="submit">Generate</button>
            </form>
        </body>
        </html>
        "#
    )
}

async fn generate(model: web::Data<NGramLanguageModel>, form: web::Form<PromptForm>) -> impl Responder {
    let prompt = &form.prompt;
    let response = model.generate(prompt, 10);
    HttpResponse::Ok().body(format!("Response: {}", response))
}

#[derive(serde::Deserialize)]
struct PromptForm {
    prompt: String,
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let mut ngram_model = NGramLanguageModel::new(2);
    
    // Train the model on a book or text corpus
    let book_text = "
    The village of Locon lies five miles out from Bethune, on the
    Estaires road. Now it is broken by the war: in October 1916 it was as
    comfortable and quiet a village as any four miles behind the line.
    If you had entered it at dusk, when the flashes of the guns begin
    to show, and passed by the square and the church and that trap for
    despatch-riders where the _chemin-de-fer vicinal_ crosses to the left
    of the road from the right, you would have come to a scrap of orchard
    on your left where the British cavalrymen are buried who fell in 1914.
    Perhaps you would not have noticed the graves, because they were
    overgrown and the wood of the crosses was coloured green with lichen.
    Beyond the orchard was a farm with a garden in front, full of common
    flowers, and a flagged path to the door.";
    ngram_model.train(book_text);
    
    HttpServer::new(move || {
        App::new()
            .data(ngram_model.clone())
            .route("/", web::get().to(index))
            .route("/generate", web::post().to(generate))
    })
    .bind("127.0.0.1:8000")?
    .run()
    .await
}
