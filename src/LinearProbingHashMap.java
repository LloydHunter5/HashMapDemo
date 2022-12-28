import java.util.Iterator;
// Evan Strohman
// 4/1/22
// Algorithms
// Homework #23
public class LinearProbingHashMap<Key,Value> implements Map<Key,Value>{

    private static final double MAX_LOAD_FACTOR = 0.75;
    private static final int STEP = 2;

    private Key[] keys;
    private Value[] values;
    private int size;
    private int capacity;

    public LinearProbingHashMap(int minCapacity){
        this.capacity = Primes.nextProbablePrime(minCapacity);
        this.keys = (Key[])new Object[capacity];
        this.values = (Value[]) new Object[capacity];
        this.size = 0;
    }

    public LinearProbingHashMap(){
        this(751);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public boolean contains(Key key) {
        return find(key) != null;
    }

    @Override
    public Value find(Key key) {
        int index = hash(key);
        while(keys[index] != null) {
            if (keys[index].equals(key)) return values[index];
            index = (index + 1) % capacity();
        }
        return null;
    }

    @Override
    public void add(Key key, Value value) {
        int index = hash(key);
        while(keys[index] != null){
            if (keys[index].equals(key)){
                values[index] = value;
                return;
            }
            index = (index + 1) % capacity();
        }
        keys[index] = key;
        values[index] = value;
        this.size++;
        //Resizes the map if it's too full
        if(loadFactor() > MAX_LOAD_FACTOR) resize(Primes.nextProbablePrime(STEP * this.capacity));
    }
    public void resize(int newCapacity){
        // Allocates new key & value arrays while storing the old ones
        Key[] tempKeys = this.keys;
        Value[] tempVals = this.values;
        this.keys = (Key[]) new Object[newCapacity];
        this.values = (Value[]) new Object[newCapacity];
        this.capacity = newCapacity;
        this.size = 0;

        // Re-hashes all key-value pairs into the new map
        for(int i = 0; i < tempKeys.length; i++){
            if(tempKeys[i] != null){
                this.add(tempKeys[i], tempVals[i]);
            }
        }
    }
    @Override
    public void remove(Key key) {
        //Finds the item to remove
        int index = hash(key);
        while(!keys[index].equals(key)) {
            if(keys[index] == null) return;
            index = (index + 1) % capacity();
        }
        //Removes item
        keys[index] = null;
        values[index] = null;

        //fixing other indices
        int tempIndex = (index+1) % capacity();
        while(keys[tempIndex] != null){
            if(hash(keys[tempIndex]) != tempIndex){
                keys[index] = keys[tempIndex];
                values[index-1] = values[tempIndex];
                tempIndex = index;
                index = (index+1) % capacity();
            }else{
                return;
            }
        }
    }
    public double loadFactor(){
        return this.size() / (double) this.capacity();
    }
    private int hash(Key key){
        if(key instanceof String){
            int hash = 7;
            String k = (String) key;
            for (int i = 0; i < k.length(); i++) {
                hash = (hash*31 + k.charAt(i)) % capacity();
            }
            return hash;
        }
        return (key.hashCode() & 0x7FFFFFFF) % this.capacity();
    }

    @Override
    public void traverse(Visit visit) {
        for(int i = 0; i < keys.length; i++){
            if(keys[i] != null) visit.visit(keys[i], values[i]);
        }
    }

    @Override
    public Iterator<Key> iterator() {
        return new MapIterator(this);
    }

    public class MapIterator implements Iterator{

        private LinearProbingHashMap<Key,Value> map;
        private int index;
        public MapIterator(LinearProbingHashMap map){
            this.map = map;
        }
        public boolean hasNext(){
            while(this.map.keys[index] == null){
                index++;
                if(index == this.map.capacity()-1) return false;
            }
            return true;
        }
        public Key next(){
            return this.map.keys[index++];
        }

    }
}
