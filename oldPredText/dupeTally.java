package oldPredText;
import java.util.*;

public class dupeTally
{
    private Map<String,Integer> map;

    public dupeTally()
    {
        map = new TreeMap<String,Integer>();
    }

    public void addWord(String word)
    {
        if(map.get(word)!=null)
        {
            Integer yessir=map.get(word);//cant do this but ?!?!?!?!?!();//store it and how amny there already are
            map.put(word, yessir+1);
        }
        else
        {
            map.put(word,1);
        }
    }

    public int getCount(String word)
    {
        if(map.get(word)==null)
        {
            return 0;
        }
        return map.get(word);
    }

    public int getTotal()
    {
        int ret=0;
        Set<String> keys= map.keySet();
        for(String x: keys)
        {
            ret=ret+getCount(x);
        }
        return ret;
    }

    public ArrayList<String> getWords()
    {
        ArrayList<String> yessir = new ArrayList<String>();
        Set<String> keys= map.keySet();
        for(String x: keys)
        {
            yessir.add(x);
        }
        for(int perhaps=0; perhaps<yessir.size(); perhaps++)
        {
            for(int maybe=perhaps+1; maybe<yessir.size(); maybe++)
            {
                if(map.get(yessir.get(perhaps))<map.get(yessir.get(maybe)))
                {
                    String tem1=new String(yessir.get(perhaps));
                    String tem2=new String(yessir.get(maybe));
                    yessir.set(perhaps, tem2);
                    yessir.set(maybe, tem1);
                }
            }
        }
        return yessir;
    }
}
