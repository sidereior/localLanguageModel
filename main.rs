use std::collections::HashMap;
use rand::seq::SliceRandom;
use rand::thread_rng;
use actix_web::{web, App, HttpResponse, HttpServer, Responder};

#[derive(Clone)]
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

            self.ngrams.entry(words[i..(i+self.n)].to_vec()).or_insert(Vec::new()).push( words[i+self.n].clone());
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
    HttpResponse::Ok().body(format!("Response: {}", model.generate(&form.prompt, 10)))
}

#[derive(serde::Deserialize)]
struct PromptForm {
    prompt: String,
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let mut ngrammodel = NGramLanguageModel::new(2);
    
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
    flowers, and a flagged path to the door.
    User: Hello, Book AI! How are you today?
Book AI: Hello! I'm an AI powered by a language model trained on a book. I don't have feelings, but I'm here to assist you. How can I help you today?

User: That's great to hear! I wanted to ask you about the book you were trained on. Can you tell me more about it?
Book AI: Of course! The book I was trained on is called The Village of Locon. It's set in a small village during the time of war and explores the lives of the villagers. It offers a glimpse into their experiences, the impact of the conflict, and the surrounding landscape. Feel free to ask me anything specific about the book!

User: What is the main theme or message conveyed in The Village of Locon?
Book AI: The main theme of The Village of Locon revolves around the contrast between the tranquility of village life and the disruptive nature of war. It explores the resilience of the villagers in the face of adversity and the profound impact that war has on their lives and surroundings. It also highlights the importance of community, memory, and the enduring human spirit.

User: That sounds fascinating. Could you generate a response based on the prompt, In the midst of chaos, hope still prevailed?
Book AI: Certainly! Here's a generated response based on your prompt: In the midst of chaos, hope still prevailed. The villagers of Locon, though surrounded by the horrors of war, clung to a flickering flame of optimism. They supported each other, finding solace in their shared struggles and cherishing the small moments of respite. It was through their unwavering hope that they found the strength to carry on, believing that someday, peace would prevail and their beloved village would be restored to its former tranquility.

User: That response captures the essence beautifully. Thank you, Book AI! Is there anything else you can share about the book or any other topic you have knowledge about?
Book AI: I'm glad you liked the response! As an AI language model, I can provide information on a wide range of topics beyond the book. Feel free to ask me anything you'd like to know, and I'll do my best to assist you.

User: That's wonderful! How about you tell me about the author of The Village of Locon?
Book AI: The Village of Locon is a fictional book created for the purpose of this conversation, and therefore it does not have a specific author associated with it. However, if you'd like information about real authors or famous works, I can certainly provide details on those.

User: I see. In that case, could you share some interesting trivia about well-known authors?
Book AI: Certainly! Here's an interesting trivia: Did you know that J.R.R. Tolkien, the author of The Lord of the Rings, invented several languages for his fictional world of Middle-earth? He was not only a talented writer but also a linguist, and he developed languages like Elvish, Dwarvish, and others to give depth and authenticity to his fantasy world.

User: That's fascinating! Tolkien's dedication to world-building is truly impressive. Thank you for sharing that trivia. I've enjoyed our conversation so far, Book AI!
Book AI: You're welcome! I'm glad you found it interesting. I'm here anytime you'd like to have a conversation or need assistance with anything else. Just let me know!
";
    ngrammodel.train(book_text);
    
    HttpServer::new(move || {
        App::new()
            .data(ngrammodel.clone())
            .route("/", web::get().to(index))
            .route("/generate", web::post().to(generate))
    })
    .bind("127.0.0.1:8000")?
    .run()
    .await
}
