package oldPredText;

import java.util.*;

public class dupeTextPredictor
{
    private Map<String,Tally> map;

    public dupeTextPredictor()
    {
        map = new TreeMap<String,Tally>();
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
    
    
}

