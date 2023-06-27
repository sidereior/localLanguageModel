package oldPredText;
import java.util.*;

public class SimpleMap<K,V>
{
    private ArrayList<Entry<K,V>> entries;

    public SimpleMap()
    {
        entries = new ArrayList<Entry<K,V>>();
    }

    public int size()
    {
        return entries.size();
    }

    public V get(Object key)
    {
        for(int x=0; x<entries.size(); x++)
        {
            if(entries.get(x).getKey().equals(key))
            {
                return entries.get(x).getValue();
            }
        }
        return null;
    }

    public V put(K key, V value)
    {
        for(int x=0; x<entries.size(); x++)
        {
            if(entries.get(x).getKey().equals(key))
            {
                V y=entries.get(x).getValue();
                entries.get(x).setValue(value);
                return y;
            }
        }
        entries.add(new Entry<K,V>(key, value));
        return null;
    }

    public V remove(Object key)
    {
        
        for(int x=0; x<entries.size(); x++)
        {
            if(entries.get(x).getKey().equals(key))
            {
                V y=entries.get(x).getValue();
                //remove the value
                entries.get(x).setValue(null);//does this actualyl remove it?
                entries.remove(x);
                return y;
            }
        }
        return null;
    }

    public SimpleSet<K> keySet()
    {
        SimpleSet<K> x = new SimpleSet<K>();
        for(Entry<K,V> y: entries)
        {
            x.add(y.getKey());
        }
        return x;
    }

    public String toString()
    {
        return entries.toString();
    }
}