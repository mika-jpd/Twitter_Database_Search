# Twitter Database
This is a Twitter database using Hash Tables to organize a Twitter messages for easy extraction of tweet authors, tweet content, and Twitter trending topics.

##Description

The point of this project was to organize Twitter tweet so that various information can be easily accessible.
I used a hashtables in order to efficiently store and access tweets written by an author and get tweets written at a certain date. One hashtable takes in as key the author and as value the tweet written by the author. The other takes in as key the date of a tweet and value an arraylist of tweets posted on that day. The conditions of adding to the Hashtable are the following:
* If a HashPair with the key already exists in the hash table, then the add function overwrite the old value associated with the key with the new one
* If in the hash table there was no previous value associated to the given key, then the method overwrites it with the new value and returns the old one
* If there was no value associated to the given key the method returns null

The other big part of the project is the trendingTopics() method whose aim is to identify the most used words, that are not stopping words, by counting the number of appearances a given word in all the tweets stored in the hash table. 

##Code Explanation 

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

##Running Instructions
