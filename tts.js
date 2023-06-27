const tts = require('google-tts-api-promise');

const exampleText = 'Hello, world!';
const outputFile = 'output.mp3';

tts
  .getAudioUrl(exampleText, {
    lang: 'en',
    slow: false,
    host: 'https://translate.google.com',
  })
  .then((url) => {
    const download = require('download');
    return download(url, './' + outputFile);
  })
  .then(() => {
    console.log('Speech saved to', outputFile);
  })
  .catch((err) => {
    console.error('Error:', err);
  });
  const fs = require('fs');
  const { promisify } = require('util');
  const axios = require('axios');
  const googleTTS = require('google-tts-api');
  
  const writeFileAsync = promisify(fs.writeFile);
  
  async function textToSpeech(text, language, outputPath) {
    try {
      const url = googleTTS.getAudioUrl(text, {
        lang: language,
        slow: false,
        host: 'https://translate.google.com',
      });
  
      const response = await axios.get(url, { responseType: 'arraybuffer' });
  
      await writeFileAsync(outputPath, response.data);
  
      console.log('Text converted to speech successfully!');
    } catch (error) {
      console.error('Error converting text to speech:', error.message);
    }
  }
  