import java.util.Iterator;

// Evan Strohman
// 3/13/22
// Algorithms
// Homework #22
public class SeparateChainingHashMap<Key, Value> implements Map<Key, Value> {
    
    public class Pair {

        private Key   key;
        private Value value;
    
        public Pair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        public Key key() {
            return this.key;
        }

        public Value value() {
            return this.value;
        }
    }


    private class List implements Iterable<Pair> {

        private class Node {
            
            private Key   key;
            private Value value;
            private Node  next;

            public Node(Key key, Value value) {
                this.key = key;
                this.value = value;
                this.next = null;
            }
        }


        private Node head;

        public List() {
            this.head = null;
        }

        public boolean contains(Key key) {
            Node rover = this.head;
            while (rover != null) {
                if (rover.key.equals(key)) return true;
                rover = rover.next;
            }
            return false;
        }

        public Value find(Key key) {
            Node rover = this.head;
            while (rover != null) {
                if (rover.key.equals(key)) return rover.value;
                rover = rover.next;
            }
            return null;
        }

        public boolean add(Key key, Value value) {
            Node rover = this.head;
            while (rover != null) {
                if (rover.key.equals(key)) {
                    rover.value = value;
                    return false;
                }
                rover = rover.next;
            }

            Node node = new Node(key, value);
            node.next = this.head;
            this.head = node;
            return true;
        }

        public boolean remove(Key key) {
            Node current = this.head;
            Node previous = null;
            while (current != null) {
                if (current.key.equals(key)) {
                    if (previous != null) {
                        previous.next = current.next;
                    } else {
                        this.head = current.next;
                    }
                    return true;
                }
                previous = current;
                current = current.next;
            }
            return false;
        }


        public Iterator<Pair> iterator() {
            return new ListIterator();
        }


        private class ListIterator implements Iterator<Pair> {

            private Node current = List.this.head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Pair next() {
                Pair result =  new Pair(current.key, current.value);
                current = current.next;
                return result;
            }
        }
    }


    private static class Array<T> implements Iterable<T> {

        private T[] array;

        public Array(int size) {
            this.array = (T[]) new Object[size];
        }

        public int size() {
            return this.array.length;
        }

        public T get(int index) {
            return this.array[index];
        }

        public void set(int index, T value) {
            this.array[index] = value;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator();
        }

        public class ArrayIterator implements Iterator<T> {

            private int current = 0;

            @Override
            public boolean hasNext() {
                return this.current < Array.this.array.length;
            }

            @Override
            public T next() {
                return Array.this.array[this.current++];
            }
        }
    }


    private Array<List> map;
    private int capacity;
    private int size;
    
    private int hash(Key key) {
        return (key.hashCode() & 0x7FFFFFFF) % this.capacity;
    }
        
    public SeparateChainingHashMap(int capacity) {
        this.map = new Array<>(capacity);
        this.capacity = capacity;
        this.size = 0;
        
        for (int i = 0; i < capacity; i++) {
            this.map.set(i, new List());
        }
    }

    public SeparateChainingHashMap(){
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
        return this.map.get(hash(key)).contains(key);
    }
    
    @Override
    public Value find(Key key) {
        return this.map.get(hash(key)).find(key);
    }
    
    @Override
    public void add(Key key, Value value) {
        boolean added = this.map.get(hash(key)).add(key, value);
        if (added) this.size++;
    }
    
    @Override
    public void remove(Key key) {
        boolean removed = this.map.get(hash(key)).remove(key);
        if (removed) this.size--;
    }

    public void traverse(Visit visit) {
        int remaining = this.size();
        for(List l : map){
            if(remaining <= 0) return;
            //if a list is not empty, traverse it
            if(l.head != null){
                //visits each pair within the list
                for(Pair p : l){
                    visit.visit(p.key,p.value);
                    remaining--;
                }
            }
        }
    }

    public Iterator<Key> iterator() {
        return new MapIterator(this.map, this.size());
    }

    public class MapIterator implements Iterator<Key>{
        private int size;
        private Array<List> map;
        private int i;
        private Iterator<Pair> listIterator;
        public MapIterator(Array map, int size){
            this.size = size;
            this.map = map;
        }
        public boolean hasNext(){
            return size > 0;
        }
        public Key next(){
            //Prevents NullPointerException
            if(listIterator == null){}
            //Returns the next item in a found list
            else if(listIterator.hasNext()) {
                size--;
                return listIterator.next().key;
            }

            //Finds next list with items in it, returns the head
            while(map.get(i).head == null) i++;
            listIterator = map.get(i).iterator();
            size--;
            i++;
            return listIterator.next().key;
        }
    }
}
