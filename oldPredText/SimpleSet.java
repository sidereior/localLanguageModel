package oldPredText;
import java.util.*;

public class SimpleSet<E>
{
  private ArrayList<E> elements;
  
  public SimpleSet()
  {
    elements = new ArrayList<E>();
  }
  
  public int size()
  {
    return elements.size();
  }
  
  public boolean contains(Object obj)
  {
    for(int x=0; x<elements.size(); x++)
    {
        if(elements.get(x).equals(obj))
        {
            return true;
        }
    }
    return false;
  }
  
  public boolean add(E obj)
  {
    if(elements.contains(obj))
    {
        return false;
    }
    else
    {
        elements.add(obj);
        return true;
    }
  }
  
  public boolean remove(Object obj)//why object obj??
  {
    if(elements.contains(obj))
    {
        int x=0;
        while(!elements.get(x).equals(obj))
        {
            x++;
        }
        elements.remove(x);
        return true;
    }
    else
    {
        return false;
    }
  }
  
  public String toString()
  {
    return elements.toString();
  }
}