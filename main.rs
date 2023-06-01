use std::collections::HashMap;
use rand::seq::SliceRandom;
use rand::thread_rng;

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

fn main() {
    let mut ngram_model = NGramLanguageModel::new(2);
    
    // Train the model on a book or text corpus
    let book_text = "Insert your book text here.";
    ngram_model.train(book_text);
    
    // Generate a response based on a prompt
    let prompt = "How does the main character feel";
    let response = ngram_model.generate(prompt, 10);
    
    println!("Response: {}", response);
}
