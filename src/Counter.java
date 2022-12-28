public class Counter {
    private int count;
    public Counter(int startingValue){
        this.count = startingValue;
    }
    public Counter(){
        this(0);
    }
    public void increment(){
        count++;
    }
    public int getCount(){
        return count;
    }
}
