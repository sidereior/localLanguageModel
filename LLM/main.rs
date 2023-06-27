use std::collections::HashMap;
use rand::seq::SliceRandom;
use rand::thread_rng;

struct LocalLanguageModel {
    word_map: HashMap<String, Vec<String>>,
}

impl LocalLanguageModel {
    fn new() -> Self {
        LocalLanguageModel {
            word_map: HashMap::new(),
        }
    }

    fn train(&mut self, text: &str) {
        let words: Vec<String> = text.split_whitespace().map(|w| w.to_string()).collect();

        for i in 0..(words.len() - 1) {
            let current_word = words[i].clone();
            let next_word = words[i + 1].clone();

            self.word_map.entry(current_word).or_insert(Vec::new()).push(next_word);
        }
    }

    fn generate(&self, prompt: &str, length: usize) -> String {
        let mut output: Vec<String> = prompt.split_whitespace().map(|w| w.to_string()).collect();
        let mut rng = thread_rng();
        let mut current_word = output.last().cloned();

        for _ in 0..length {
            if let Some(word) = current_word.clone() {
                if let Some(next_words) = self.word_map.get(&word) {
                    let next_word = next_words.choose(&mut rng).unwrap().clone();
                    output.push(next_word.clone());
                    current_word = Some(next_word);
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        output.join(" ")
    }
}

fn main() {
    let mut model = LocalLanguageModel::new();

    // Train the model on a book or text corpus
    let book_text = "
    The village of Locon lies five miles out from Bethune, on the
    Estaires road. Now it is broken by the war: in October 1916 it was as
    comfortable and quiet a village as any four miles behind the line.
    ";
    model.train(book_text);

    // Generate a response based on a prompt
    let prompt = "The village of Locon";
    let response = model.generate(prompt, 10);

    println!("Response: {}", response);
}
