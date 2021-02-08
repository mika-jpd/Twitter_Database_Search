package FinalProject_Template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
        buckets = new ArrayList<LinkedList<HashPair<K,V> > >(initialCapacity);

        for(int i = 0; i < initialCapacity; i++) {
            LinkedList<HashPair<K, V> > linked = new LinkedList<HashPair<K, V> >();
            buckets.add(linked);
        }

        numBuckets = initialCapacity;
        numEntries = 0;
    }
    
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }

    public void printHash() {
        int i = 0;
        for (LinkedList<HashPair<K,V> > list : buckets){
            System.out.print("\n" + i + " : ");
            for (HashPair<K,V> pair : list) {
                System.out.print(pair + ", ");
            }
            i++;
        }  
        
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {

        //System.out.println("Adding : " + key);
        HashPair<K,V> add = new HashPair<K,V>(key, value);
        double y = ((double)numEntries + 1)/(double)numBuckets;
        //System.out.println(y);
        if (y > MAX_LOAD_FACTOR){
            //System.out.println("Hit rehash");
            rehash();
        }
        if (add.getValue() == null) {
            System.out.println(key + " : null value !");
            return null;
        }

        int x = hashFunction(key);

        // If there is nothing at an index, then LinkedList.add
        if ((buckets.get(x)).size() == 0){
            buckets.get(x).add(add);
            //System.out.println("Add empty linkedList");
            numEntries++;
            return add.getValue();
        }
        //
        /*
            If the LinkedList is not empty: set temp to the head, use enhanced for loop
            to go thrugh the LinkedList and compare. If success, replace and return old
            value.
        */
        else {
            
           
            //HashPair<K,V> temp = buckets.get(x).getFirst();
            
            for (HashPair<K,V> pair : buckets.get(x)) {
                if (pair.getKey().equals(key)) {
                    V val = pair.getValue();
                    pair.setValue(value);
                    return val;
                }
            }
        }
        buckets.get(x).add(add);
        numEntries++;

    	return add.getValue();
    }
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    
    public V get(K key) {
        int hashCode = hashFunction(key);

        if (buckets.get(hashCode).size() == 0) return null;
        
        else {
            for(HashPair<K,V> pair : buckets.get(hashCode)) {
                if (pair.getKey().equals(key)){
                    //System.out.println("Get returning : " + pair.getValue() + " because " + pair.getKey() + " = " + key + " which is " + pair.getKey().equals(key));
                    return pair.getValue();
                }
                
            }
        }

        return null;
    }
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1)
     * Returns the value of the removed key
     * If no key, return null 
     */
    public V remove(K key) {
        int hashCode = hashFunction(key);

        if (buckets.get(hashCode).size() == 0) return null;

        else {
            for(HashPair<K,V> pair : buckets.get(hashCode)) {
                if (pair.getKey().equals(key)){
                    V value = pair.getValue();
                    buckets.get(hashCode).remove(pair);
                    numEntries--;
                    return value;
                }
            }
        }
        
    	return null;

    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */

    public void rehash() {
        numBuckets = numBuckets*2;
        MyHashTable<K,V> temp = new MyHashTable<>(numBuckets);
        //buckets = new ArrayList<LinkedList<HashPair<K,V> > >(numBuckets);

        for (LinkedList<HashPair<K,V> > list : this.buckets) for(HashPair<K,V> pair : list) temp.put(pair.getKey(), pair.getValue());

        this.buckets = temp.buckets;
    }
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    
    public ArrayList<K> keys() {
        ArrayList<K> key$ = new ArrayList<K>(numEntries);
        
        for (LinkedList<HashPair<K, V> > list : buckets) {
            for (HashPair<K,V> pair : list) key$.add(pair.getKey());
        }
    	return key$;
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
        ArrayList<V> keyys = new ArrayList<V>();
        
        for (LinkedList<HashPair<K, V> > list : this.buckets) {
            for (HashPair<K,V> pair : list){
                if (keyys.contains(pair.getValue()));
                else {
                    keyys.add(pair.getValue());
                }
            } 
        }
    	return keyys;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults;
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        ArrayList<HashPair<K,V> > toSort = new ArrayList<HashPair<K,V>>(results.numEntries);
        ArrayList<K> sorted = new ArrayList<K>(results.numEntries);
        for(LinkedList<HashPair<K,V> > list : results.getBuckets()) for(HashPair<K,V> pair : list) toSort.add(pair);
        
        //System.out.println("This is right index : " + (toSort.size()-1));
        //System.out.println("Before quickSort");
        //for (HashPair<K,V> pair : toSort) System.out.print(pair.getKey() + " , ");
        //System.out.println("\n------------");
        quicksort(toSort, 0, toSort.size() - 1);
        
        for (HashPair<K,V> pair : toSort) sorted.add(pair.getKey());
        //System.out.println("\nAfter : ");
        return sorted;
    }

    private static <K, V extends Comparable <V> > void quicksort(ArrayList<HashPair<K,V> > toSort, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) {
        } else {
            int i = placeAndDivide(toSort, leftIndex, rightIndex);
            
            quicksort(toSort, leftIndex, i-1);
            quicksort(toSort, i+1, rightIndex);
        }
    }

    private static <K, V extends Comparable <V> > int placeAndDivide (ArrayList<HashPair<K,V> > toSort, int leftIndex, int rightIndex) {
        V pivot = toSort.get(rightIndex).getValue();
        int wall = leftIndex - 1;
        //System.out.println("Pivot : " + pivot + " Wall : "+ wall + " Right Index : " + rightIndex);
        //System.out.println(toSort);

        for (int i = leftIndex; i < rightIndex; i++) {
            if (toSort.get(i).getValue().compareTo(pivot) > 0) {
                //System.out.println("Increase wall for below");
                wall++;
                HashPair<K,V> temp = toSort.get(i);
                toSort.set(i, toSort.get(wall));
                toSort.set(wall, temp);
            }
            //System.out.println("\n\n\n"+ i + " Wall : " + wall);
            //System.out.println(toSort);
        }
        HashPair<K,V> temp = toSort.get(rightIndex);
        toSort.set(rightIndex, toSort.get(wall+1));
        toSort.set(wall +1, temp);
        //System.out.println("\n\n\n"+toSort);
        //System.out.println("------------");
        return wall + 1;
        
    }
    
    
    
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
        ArrayList<HashPair<K,V> > pairs;
        int index;
    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
            pairs = new ArrayList<HashPair<K,V> >(numEntries);
            for(LinkedList<HashPair<K,V> > list : buckets) for(HashPair<K,V> pair : list) pairs.add(pair);
            index = -1;
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            if (index + 1 <= this.pairs.size() -1 ) {
                return true;
            }
        	return false;
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            if (hasNext()) {
                index++;
                return this.pairs.get(index);
            }
        	return null;
        }
        
    }
}
