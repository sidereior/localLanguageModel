package oldPredText;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class PredictDisplay implements ActionListener
{

    private static final int FONT_SIZE = 24;

    private static final int MAX_PREDICTIONS = 5;

    public static void main(String[] args)
    {
        new PredictDisplay("good.txt");
    }

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JPanel buttonPanel;
    private JButton[] buttons;
    private TextPredictor predictor;

    public PredictDisplay(String file)
    {
        predictor = new TextPredictor();
        load(file);

        frame = new JFrame(file);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

        Font font = new Font(null, Font.PLAIN, FONT_SIZE);

        textArea = new JTextArea(10, 40);
        textArea.setFont(font);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);

        buttonPanel = new JPanel(new GridLayout(1, 3));
        frame.getContentPane().add(buttonPanel);

        buttons = new JButton[3];
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new JButton();
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }

        textField = new JTextField(40);
        textField.setFont(font);
        textField.setActionCommand("textField");
        textField.addActionListener(this);
        frame.getContentPane().add(textField);

        updateButtons();

        frame.pack();
        frame.setVisible(true);

        textField.requestFocus();
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        String word;
        if (command.equals("textField"))
            word = textField.getText();
        else
            word = buttons[Integer.parseInt(command)].getText();
        if (textArea.getText().length() == 0)
            textArea.setText(word);
        else
            textArea.append(" " + word);
        updateButtons();
        textField.selectAll();
    }

    private void updateButtons()
    {
        Tally tally = predictor.predict(parse(textArea.getText()));
        if (tally == null)
            tally = predictor.predict(new String[0]);
        ArrayList<String> predictions;
        if (tally != null)
        {
            predictions = tally.getWords();
            if (predictions.size() > MAX_PREDICTIONS)
            {
                ArrayList<String> shorter = new ArrayList<String>();
                for (int i = 0; i < MAX_PREDICTIONS; i++)
                    shorter.add(predictions.get(i));
                predictions = shorter;
            }
        }
        else
        {
            ArrayList<String> words = new ArrayList<String>();
            for (String word : new String[]{"the", "of", "and", "a", "to", "in", "is", "you", "that", "it"})
                words.add(word);
            int predictionsLength = Math.min(MAX_PREDICTIONS, words.size());
            predictions = new ArrayList<String>();
            for (int i = 0; i < predictionsLength; i++)
                predictions.add(words.remove((int)(Math.random() * words.size())));
        }

        for (int i = 0; i < predictions.size(); i++)
        {
            int r = (int)(Math.random() * (predictions.size() - i)) + i;
            String temp = predictions.get(i);
            predictions.set(i, predictions.get(r));
            predictions.set(r, temp);
        }

        buttonPanel.removeAll();
        buttons = new JButton[predictions.size()];
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i] = new JButton(predictions.get(i));
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }
        frame.pack();
    }

    private void load(String file)
  {
    try
    {
      Scanner in = new Scanner(new File(file));
      while (in.hasNextLine())
      {
        String line = in.nextLine();
        String[] tokens = parse(line);
        if (tokens.length > 0)
          predictor.record(tokens);
      }
      in.close();
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  private String[] parse(String text)
  {
    String[] tokens = text.split(" ");
    ArrayList<String> list = new ArrayList<String>();
    for (String token : tokens)
    {
      token = token.trim();
      if (token.length() > 0)
        list.add(token);
    }
    tokens = new String[list.size()];
    for (int i = 0; i < list.size(); i++)
      tokens[i] = list.get(i);
    return tokens;
  }

    
}