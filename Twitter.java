package FinalProject_Template;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class Twitter {
	
    public MyHashTable<String, Tweet> authorHashTable;
    public MyHashTable<String, ArrayList<Tweet> > dateHashTable; 
    public ArrayList<String> stopwords;
    // O(n+m) where n is the number of tweets, and m the number of stopWords
    
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
        this.stopwords = new ArrayList<String>();
        authorHashTable = new MyHashTable<String, Tweet>(tweets.size());
        dateHashTable = new MyHashTable<String, ArrayList<Tweet> >(tweets.size());
        for (Tweet tweeties : tweets) addTweet(tweeties);
        for (String words : stopWords) this.stopwords.add(words);
        
	}
	
    
    /**
     * Add Tweet t to this Twitter
     * O(1).
     * Add the tweet to both the MyHashTables
     */
	public void addTweet(Tweet t) {
        //System.out.println("Adding : " + t);
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
	

    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
        return authorHashTable.get(author);
    }


    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {

    	return dateHashTable.get(date);
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
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
    
    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    /**
     * A helper method used to obtain the date from a dateAndTime !
     */
    private static String getDate(String dateAndTimeString) {
    	String[] words = dateAndTimeString.split(" ");
    	return words[0];
    }

    

}
