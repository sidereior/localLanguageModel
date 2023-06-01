package oldPredText;
public  class test
{
    public Tally testt()
    {
        TextPredictor tp = new TextPredictor();
        String[] seuss = {"I", "am", "Sam", "Sam", "I", "am"};
        tp.record(seuss);
        String[] test1 = {"I"};
        Tally t = tp.predict(test1);
        return t;
    }
    
    public void printer(Tally t)
    {
        for(int x=0; x<t.getWords().size(); x++)
        {
            System.out.println("key: " + t.getWords().get(x) + " value: " + t.getCount(t.getWords().get(x)));
        }
    }
}