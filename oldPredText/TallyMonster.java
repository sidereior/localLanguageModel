package oldPredText;
import java.util.*;

public class TallyMonster
{
  public static void main(String[] args)
  {
    System.out.println("Testing SimpleSet");
    SimpleSet<String> set = new SimpleSet<String>();
    test(set.size() == 0);
    test(!set.contains("A"));
    test(set.add("A"));
    test(set.size() == 1);
    test(set.contains("A"));
    test(!set.contains("B"));
    test(set.add("B"));
    test(set.size() == 2);
    test(set.contains("A"));
    test(set.contains("B"));
    test(set.add("C"));
    test(set.size() == 3);
    test(set.contains("A"));
    test(set.contains("B"));
    test(set.contains("C"));
    test(!set.contains("D"));
    test(!set.add("A"));
    test(set.size() == 3);
    test(!set.add("B"));
    test(set.size() == 3);
    test(!set.add("C"));
    test(set.size() == 3);
    test(set.add("D"));
    test(set.add("E"));
    test(set.size() == 5);
    test(!set.remove("F"));
    test(set.size() == 5);
    test(set.remove("C"));
    test(set.size() == 4);
    test(!set.contains("C"));
    test(set.remove("A"));
    test(set.size() == 3);
    test(!set.contains("A"));
    test(set.remove("E"));
    test(set.size() == 2);
    test(!set.contains("E"));
    test(set.contains("B"));
    test(set.contains("D"));
    test(set.contains("CODE".substring(2, 3)));  //makes sure uses .equals instead of ==
    test(!set.add("CODE".substring(2, 3)));
    test(set.remove("CODE".substring(2, 3)));
    set = new SimpleSet<String>();
    set.add("A");
    set.add("B");
    set.add("C");
    System.out.println("SimpleSet Passed");
    
    System.out.println("Testing SimpleMap");
    SimpleMap<String, Integer> map = new SimpleMap<String, Integer>();
    test(map.size() == 0);
    test(map.get("A") == null);
    test(map.put("A", 1) == null);
    test(map.size() == 1);
    test(map.get("A") == 1);
    test(map.get("B") == null);
    test(map.put("B", 2) == null);
    test(map.size() == 2);
    test(map.get("A") == 1);
    test(map.get("B") == 2);
    test(map.put("C", 3) == null);
    test(map.size() == 3);
    test(map.get("A") == 1);
    test(map.get("B") == 2);
    test(map.get("C") == 3);
    test(map.put("A", 4) == 1);
    test(map.size() == 3);
    test(map.get("A") == 4);
    test(map.put("B", 5) == 2);
    test(map.size() == 3);
    test(map.get("B") == 5);
    test(map.put("C", 6) == 3);
    test(map.size() == 3);
    test(map.get("C") == 6);
    test(map.put("D", 7) == null);
    test(map.put("E", 8) == null);
    test(map.get("F") == null);
    test(map.remove("F") == null);
    test(map.size() == 5);
    test(map.remove("C") == 6);
    test(map.size() == 4);
    test(map.get("C") == null);
    test(map.remove("A") == 4);
    test(map.size() == 3);
    test(map.get("A") == null);
    test(map.remove("E") == 8);
    test(map.size() == 2);
    test(map.get("E") == null);
    test(map.get("B") == 5);
    test(map.get("D") == 7);
    test(map.get("CODE".substring(2, 3)) == 7);  //makes sure uses .equals instead of ==
    test(map.get("CODE".substring(2, 3)) != null);
    test(map.put("CODE".substring(2, 3), 0) != null);
    test(map.remove("CODE".substring(2, 3)) != null);
    System.out.println("SimpleMap Passed");
    
    System.out.println("Testing Tally");
    Tally t = new Tally();
    t.addWord("hen");
    t.addWord("ring");
    t.addWord("ring");
    t.addWord("partridge");
    t.addWord("ring");
    t.addWord("hen");
    test(t.getCount("ring") == 3);
    test(t.getCount("partridge") == 1);
    test(t.getCount("hen") == 2);
    test(t.getCount("bird") == 0);
    test(t.getCount("dove") == 0);
    test(t.getTotal() == 6);
    boolean sameOrder = testList(t.getWords(), new String[]{"ring", "hen", "partridge"});
    t.addWord("dove");
    t.addWord("ring");
    t.addWord("hen");
    t.addWord("dove");
    t.addWord("ring");
    test(t.getCount("ring") == 5);
    test(t.getCount("partridge") == 1);
    test(t.getCount("hen") == 3);
    test(t.getCount("bird") == 0);
    test(t.getCount("dove") == 2);
    test(t.getTotal() == 11);
    sameOrder = sameOrder && testList(t.getWords(), new String[]{"ring", "hen", "dove", "partridge"});
    System.out.println("Tally passed");
    
    if (sameOrder)
      System.out.println("Tally.getWords() returns list in SORTED ORDER");
    
    System.out.println("Testing TextPredictor");
    TextPredictor tp = new TextPredictor();
    String[] test1 = {"a", "b", "a", "c", "c", "b", "a"};
    tp.record(test1);
    testPredict(tp, "a", "b1c1");
    testPredict(tp, "b", "a2");
    testPredict(tp, "c", "b1c1");
    testPredict(tp, "d", null);
    testPredict(tp, "bdbc", "b1c1");
    testPredict(tp, "dcdb", "a2");//da error is on the empty sting stuff and it mst be record
    testPredict(tp, "", "a1");
    String[] test2 = {"b", "b", "c", "c", "a", "c", "b"};
    tp.record(test2);
    testPredict(tp, "", "a1b1");
    testPredict(tp, "bca", "b1c2");
    testPredict(tp, "cab", "a2b1c1");//this is the new problem
    testPredict(tp, "abc", "a1b2c2");
    testPredict(tp, "bad", null);
    String[] test3 = {"a"};
    tp.record(test3);
    testPredict(tp, "", "a2b1");
    testPredict(tp, "na", "b1c2");
    testPredict(tp, "nb", "a2b1c1");
    testPredict(tp, "nc", "a1b2c2");
    testPredict(tp, "no", null);
    System.out.println("TextPredictor Passed");
  }
  
  private static void test(boolean b)
  {
    if (!b)
      throw new RuntimeException("did not pass test case");
  }
  
  private static boolean testList(ArrayList<String> list, String[] array)
  {
    test(list.size() == array.length);
    Set<String> set1 = new TreeSet<String>();
    for (String s : list)
      set1.add(s);
    test(set1.size() == array.length);
    Set<String> set2 = new TreeSet<String>();
    for (String s : array)
      set2.add(s);
    test(set1.equals(set2));
    boolean sameOrder = true;
    for (int i = 0; i < list.size(); i++)
    {
      if (!list.get(i).equals(array[i]))
        sameOrder = false;
    }
    return sameOrder;
  }
  
  private static void testPredict(TextPredictor tp, String words, String tally)
  {
    String[] a = new String[words.length()];
    for (int i = 0; i < words.length(); i++)
      a[i] = words.substring(i, i + 1);
    Tally t = tp.predict(a);
    if (tally == null)
    {
      test(t == null);
      return;
    }
    test(t != null);
    int total = 0;
    for (int i = 0; i < tally.length(); i += 2)
    {
      int count = t.getCount(tally.substring(i, i + 1));
      test(count == Integer.parseInt(tally.substring(i + 1, i + 2)));
      total += count;
    }
    test(t.getTotal() == total);
  }
}