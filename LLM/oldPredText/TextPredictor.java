package oldPredText;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class TextPredictor
{
    private Map<String,Tally> map;

    public TextPredictor()
    {
        map = new TreeMap<String,Tally>();
    }

    private static TextPredictor predictor;
    public static void main(String[] args, int lengthOfStory)
    {
        predictor = new TextPredictor();
        predictor.storyGenerator(lengthOfStory);
    }

    public void record(String[] words)
    {

        // for(int a=0; a<words.length-1; a++)//not going to last spot
        // {
        // Tally t= map.get(words[a]);//this is going ot be null
        // if(t!=null)
        // {
        // t.addWord(words[a+1]); 
        // map.put(words[a],t);
        // }
        // else
        // {
        // Tally x= new Tally();
        // x.addWord(words[a+1]);
        // map.put(words[a],x);
        // }
        // }
        // if(words.length>0)
        // {
        // Tally t= map.get("");//this is going ot be null
        // if(t!=null)
        // {
        // t.addWord(words[0]); 
        // map.put(words[0],t);
        // }
        // else
        // {
        // Tally x= new Tally();
        // x.addWord(words[0]);
        // map.put("",x);
        // }
        // }   

        //this is the next

        // for(int i=0; i<words.length-1; i++)
        // {
        // if(map.get(words[i])==null)
        // {
        // Tally t=new Tally();
        // t.addWord(words[i+1]);
        // map.put(words[i],t);
        // }
        // else 
        // {
        // Tally t=map.get(words[i]);
        // t.addWord(words[i]);
        // map.put(words[i],t);
        // }
        // }
        // Tally t = new Tally();
        // t.addWord(words[0]);
        // map.put("",t);
        //I NEED TO RECORD THE FIRST ONE AS "" AND ITS PREVIOUS

        for(int i = 0; i < words.length - 1; i++)
        {
            if(map.get(words[i]) != null)
                map.get(words[i]).addWord(words[i+1]);
            else//dont need first statement within lese
            {
                Tally t = new Tally();
                t.addWord(words[i+1]);
                map.put(words[i], t);
            }
        }//now do ""
        if(map.get("") == null)
        {
            Tally t = new Tally();
            t.addWord(words[0]);//PUT IN TO THE TALLY IF NOT PRSENTG
            map.put("", t);
        }
        else if(map.get("") != null)
        {
            map.get("").addWord(words[0]);
        }

    }

    public Tally predict(String[] recentWords)
    {
        String x;
        if(recentWords.length>0)
            x=recentWords[recentWords.length-1];
        else
            x="";
        return map.get(x);
    }

    public Set<String> keySet()
    {
        return map.keySet();
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
                    record(tokens);
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

    public void storyGenerator(int length)
    {
        String last=new String("");
        load("epic.txt");
        int loop=0;
        String[] r={""};
        Tally tally = predictor.predict(r);
        //this is the problem
        System.out.println(last);

        while(loop<length)
        {

            //NPE ON fish=tally.getWords() so either getWords is messed up or (likely this one)
            //or prse method doesn't work as intented and the seperation of the txt file is not
            //good.
            //everything below is selecting the word you WANT TO PREDICT 
            //and PRINTING OUT THE ALREADY PREDICTED WORD!!!!!!!
            if(tally!=null)
            {
                ArrayList<String> fish = tally.getWords();//this is the problem!

                ArrayList<Integer> bang=new ArrayList<Integer>();
                for(int x=0; x<fish.size(); x++)
                {
                    //get it at each sopot and then make valeu stinrgn
                    bang.add(tally.getCount(fish.get(x)));
                    //after get the values add them all up and get each proportion and have a bunch of if statements
                }

                //next is the 55555 333 22 thing
                ArrayList<Integer> next=new ArrayList<Integer>();
                boolean bonk=true;
                while(bonk==true)
                {
                    int big;
                    for(int z=0; z<bang.size(); z++)
                    {
                        big=bang.get(z);
                        for(int x=0; x<big; x++)
                        {
                            next.add(bang.get(z));
                        }
                    }
                    bonk=false;
                }
                //now i have the 5555 44 4 33 3 2 etc
                int whole=bang.size();
                int index=((int)(Math.random()*whole));
                int value=next.get(index);
                int bindex=0;
                for(int x=0; x<bang.size(); x++)
                {
                    if(bang.get(x)==value)
                    {
                        bindex=x;
                    }
                }
                String selected=fish.get(bindex);
                String space=" ";
                System.out.print(space + selected);
                String[] sel= {selected};
                tally=predictor.predict(sel);//THIS IS THE PROBLEM
                if(tally==null)
                {
                    int wholee=bang.size();
                    int indexx=((int)(Math.random()*wholee));
                    int valuee=next.get(indexx);
                    int bindexx=0;
                    for(int x=0; x<bang.size(); x++)
                    {
                        if(bang.get(x)==valuee)
                        {
                            bindexx=x;
                        }
                    }
                    String selectedd=fish.get(bindexx);
                    String spacee=" ";
                    System.out.print(spacee + selectedd);
                    String[] sell= {selectedd};
                    tally=predictor.predict(sell);
                }

                loop++;
            }
            else
            {
                break;
            }
        }

    }
}
