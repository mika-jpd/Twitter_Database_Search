# Twitter Database
## Table of Contens
* [Description](#description)
* [Code Explanation](#code-explanation)
* [Run code](#run-code)

## Description

The point of this project was to organize Twitter tweet using hashtables in order to efficiently store and access tweets written by an author and get tweets written at a certain date. One hashtable takes in as key the author and as value the tweet written by the author. The other takes in as key the date of a tweet and value an arraylist of tweets posted on that day. The conditions of adding to the Hashtable are the following:
* If a HashPair with the key already exists in the hash table, then the add function overwrite the old value associated with the key with the new one
* If in the hash table there was no previous value associated to the given key, then the method overwrites it with the new value and returns the old one
* If there was no value associated to the given key the method returns null

The other big part of the project is the trendingTopics() method whose aim is to identify the most used words, that are not stopping words, by counting the number of appearances a given word in all the tweets stored in the hash table. 

## Code Explanation 

Twitter:
* addTweet(Tweet t): adds a tweet to both hashtables
*   * where we have *MyHashTable<String, Tweet> authorHashTable* as a class variable
*   * and we have *MyHashTable<String, ArrayList<Tweet> > dateHashTable* is a class variable
```
public void addTweet(Tweet t) {
        if (authorHashTable.get(t.getAuthor())== null) {
            authorHashTable.put(t.getAuthor(), t);
        }
        else {
            if (authorHashTable.get(t.getAuthor()).compareTo(t) < 0) {
                authorHashTable.put(t.getAuthor(), t);
            }
        }
        
        ArrayList<Tweet> temp = new ArrayList<Tweet>();
        temp.add(t);
        String date = getDate(t.getDateAndTime());
        ArrayList<Tweet> returned = dateHashTable.put(date, temp);
        if (!temp.equals(returned)) {
            returned.add(t);
            dateHashTable.put(date, returned);
        }
	}
```
* trendingTopics():
*   * returns an ArrayList of words (that are not stop words!) that appear in the tweets.
*   * the words are ordered from most frequent to least frequent by counting in how many tweet messages the words appear
*   * if a word appears more than once in the same tweet, it will be counted only once
*   * I have dateHashTable.size()*350 because tweets are 350 characters long
```
    public ArrayList<String> trendingTopics() {
        MyHashTable<String, Integer> table = new MyHashTable<String, Integer>(dateHashTable.size()*350);
        for (HashPair<String, ArrayList<Tweet> > pair : dateHashTable){
            for (Tweet tweet : pair.getValue()) {
                ArrayList<String> single =  new ArrayList<String>();
                for (String w : getWords(tweet.getMessage())) if (!(single.contains(w))) single.add(w);

                for (String word : single) {

                    if (!stopwords.contains(word.toLowerCase())) {
                        System.out.println("For " + word + " : " + table.get(word));
                        int temp = 0;
                        int returned = table.put(word.toLowerCase(), 0);
                        System.out.println("Returned Value : " + returned + "\n");
                        if(returned == 0) {
                        returned++;
                        table.put(word.toLowerCase(), returned);
                        }
                        else {
                            returned++;
                            table.put(word.toLowerCase(), returned);
                        }
                    }
                    
                }
            }
        }
        for(HashPair<String, Integer> pair : table) System.out.println(pair);
        ArrayList<String> trends = table.fastSort(table);
        
        return trends;	
    }
```
MyHashTable:
* put(K key, V value)
*    * takes a key and a value as input and adds the corresponding HashPair to this HashTable
*    * if a key already exists but has another value in the table, we reset the value and return the old value
*    * the load factor = 0.75
*    * if the hashtable must be rehashed, the pair (k, v) is still added but to the newly rehashed table
```
public V put(K key, V value) {

        HashPair<K,V> add = new HashPair<K,V>(key, value);
        double y = ((double)numEntries + 1)/(double)numBuckets;
        if (y > MAX_LOAD_FACTOR){
            rehash();
        }
        if (add.getValue() == null) {
            System.out.println(key + " : null value !");
            return null;
        }
        int x = hashFunction(key);
        if ((buckets.get(x)).size() == 0){
            buckets.get(x).add(add);

            numEntries++;
            return add.getValue();
        }
        else {
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
```
* rehash()
*    * doubles the size of the table to decrease collision rates
```
public void rehash() {
        numBuckets = numBuckets*2;
        MyHashTable<K,V> temp = new MyHashTable<>(numBuckets);
        for (LinkedList<HashPair<K,V> > list : this.buckets) for(HashPair<K,V> pair : list) temp.put(pair.getKey(), pair.getValue());
        this.buckets = temp.buckets;
    }
```
## Run code
Just download the files and place them all in a single folder!
The Dataset class contains a couple of examples of tweets whose structure is compatible with my code. 

Enjoy!
