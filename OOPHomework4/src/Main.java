//Gilbert Isaac Salazar
//HW 4
//This program does something

import java.util.*;
import java.io.*;

/**********************************************************/
/* Feed                                                   */
/**********************************************************/

class Feed{
    private String _title;
    private String _desc;
    public Feed(){}
    public Feed(String a, String b){
        _title = a; _desc = b;
    }
    public void setTitle(String a){_title = a;}
    public void setDesc(String a){_desc = a;}
    public String getTitle(){return _title;}
    public String getDesc(){return _desc;}
    public String toString(){return _title + " " + _desc;}
}

/**********************************************************/
/* NewsFeed				                  */
/**********************************************************/

class NewsFeed {
    private ArrayList<Feed> _newsFeed;
    public NewsFeed(){_newsFeed = new ArrayList<Feed>();}
    public NewsFeed(String fileName) throws IOException {
        _newsFeed = new ArrayList<>();
        Scanner infile = new Scanner(new File(fileName));
        //Gets a new line every iteration and splits it by semicolon for title and description
        while(infile.hasNext()){
            String[] news = infile.nextLine().split(",");
            _newsFeed.add(new Feed(news[0], news[1]));
        }
        infile.close();
    }
    public Feed getRandomFeed(){
        //random number generator to pick random number from 0 to size of arraylist
        Random rand = new Random();
        int idx = rand.nextInt(0, _newsFeed.size());
        return _newsFeed.get(idx);
    }
}
/**********************************************************/
/* Strategy Pattern Interface/Classes                     */
/**********************************************************/

// Provided: Strategy Interface
interface AnalysisBehavior {
    double analyze(String[] words, String searchWord);
}

// Task: Complete Class CountIfAnalysis
//For this one, I was trying to think of ways to do this in linear time but could not find a way. I had to use two
//while loops because I needed to iterate through the words, then I needed to iterate through the letters to see
//they would match up. I did a while loop for a non-exhaustive search in order to get out of the loop if need be.
//The first loop will see if the lengths will match up and if they don't, then it will skip that word and go to the next. 
//The second loop will check each char of each word and make sure they are equivalent. I think a double would be good 
//because if someone types in letters of a word and not the whole word, it could show  that part of the the small input 
//is part of something larger. It returns 1 if it is found and 0 if it is not
class CountIfAnalysis implements AnalysisBehavior {
    public double analyze(String[] words, String searchWord){
        int i = 0;
        int j = 0;
        boolean found = false;
        boolean equality = true;
        int len = searchWord.length();
        searchWord = searchWord.toLowerCase();
        while(i < words.length && !found){
            words[i] = words[i].toLowerCase();
            if(words[i].charAt(words[i].length()-1) == ',' || words[i].charAt(words[i].length()-1) == '\''){
                words[i] = words[i].substring(0, words[i].length()-1);
            }
            equality = true;
            j = 0;
            if(words[i].length() != searchWord.length()){
                i++;
            }
            else{
                while(j < len && equality){
                    if(words[i].charAt(j) != searchWord.charAt(j)){
                        equality = false;
                    }
                    else{
                        j++;
                    }
                }
                if(equality){
                    found = true;
                }
            }
        }
        if(equality && found){
            return 1;
        }
        else{
            return 0;
        }
    }
}
// Task: Complete Class CountAllAnalysis
//I took a lot of inspriration from the last class where I would iterate through the words to find a word that matched the
//length of the word we are looking for. If it is not equal, skip the word and go to the next. If it is equal, then iterate
//through the letters of each word and check if they are equivalent. If they are equivalent, then add one to a counter that
//counts up the number of words it found. I decided to use a for loop exhaustive pattern for this one in order to go through
//all the words and determine the total number of words that were found to be the same.
class CountAllAnalysis implements AnalysisBehavior {
    public double analyze(String[] words, String searchWord){
        int j = 0;
        int len = searchWord.length();
        boolean equality = true;
        int ctr = 0;
        searchWord = searchWord.toLowerCase();
        for(int i = 0; i < words.length; i++){
            words[i] = words[i].toLowerCase();
            if(words[i].charAt(words[i].length()-1) == ',' || words[i].charAt(words[i].length()-1) == '\''){
                words[i] = words[i].substring(0, words[i].length()-1);
            }
            j = 0;
            equality = true;
            if(words[i].length() == searchWord.length()){
                while(j < len && equality){
                    if(words[i].charAt(j) != searchWord.charAt(j)){
                        equality = false;
                    }
                    else{
                        j++;
                    }
                }
                if(equality){
                    ctr++;
                }
            }
        }
        return ctr;
    }

}

/**********************************************************/
/* Observer Pattern Interface/Classes                     */
/**********************************************************/

interface Subject {  // Notifying about state changes
    void subscribe(Observer obs);
    void unsubscribe(Observer obs);
    void notifyObservers(Feed f);
}

interface Observer {  // Waiting for notification of state changes
    void update(Feed f, String platformName);
}


abstract class SocialMediaPlatform implements Subject {
    private String _name;
    private ArrayList<Feed> _feed;
    private ArrayList<Observer> _observers;
    private int _updateRate;

    public SocialMediaPlatform(String n, int x){
        _name = n;
        _feed = new ArrayList<Feed>();
        _observers = new ArrayList<Observer>();
        _updateRate = x;
    }
    public void addFeed(Feed f){_feed.add(f);}
    public Feed getFeed(int i){return _feed.get(i);}
    public int getRate(){return _updateRate;}
    public String getName(){return _name;}
    public int size(){return _feed.size();}
    public void subscribe(Observer obs){_observers.add(obs);}
    public void unsubscribe(Observer obs){_observers.remove(obs);}
    public void notifyObservers(Feed f){
        for (Observer observer : _observers)
            observer.update(f, _name);
    }
    //This gets a random number to see if it will pass the update rate and if it does then it puts the feed
    //into the ArrayList and notifies the observers with the feed.
    public void generateFeed(NewsFeed nf){
       Random rand = new Random();
       int x = rand.nextInt(0, 100);
       if(x <= _updateRate){
            Feed f = nf.getRandomFeed();
            addFeed(f);
            notifyObservers(f);
       }
    }
    //Get random feed to analyze, convert it to a String array, thenuse the analysis behavior to get the number
    public double analyzeFeed(String w, AnalysisBehavior ab){
        Random rand = new Random();
        int idx = rand.nextInt(0, _feed.size()-1);
        Feed f = _feed.get(idx);
        String[] words = f.getDesc().split(" ");
        return ab.analyze(words, w);
    }
    public String toString(){
        String s = "";
        for (Feed f: _feed)
            s = s + f.getTitle() + ", " + f.getDesc() + "\n";
        return s;
    }
}

// Concrete Social Media Platforms
class Instagram extends SocialMediaPlatform {
    public Instagram() {
        super("Instagram", 30);  // 30% update rate
    }
}
//I chose Facebook because it is a very well known platform with many users
//and a 50% update rate because many people use this smp so it updates frequently
class Facebook extends SocialMediaPlatform{
    public Facebook(){
        super("Facebook", 50); //50% update rate
    }
}
//I chose Twitter (X) because this is where news gets around the quickest and
//many people use this to also spread misinformation. I chose 80% update rate
//because many people tweet about news and misinformation all the time on this app
class Twitter extends SocialMediaPlatform{
    public Twitter(){
        super("Twitter", 80); //80% update rate
    }
}

class User implements Observer{
    private String _name;
    private ArrayList<SocialMediaPlatform> _myfeeds;
    public User(){_myfeeds = new ArrayList<SocialMediaPlatform>();}
    public User(String s){
        _name = s;
        _myfeeds = new ArrayList<SocialMediaPlatform>();
    }
    public void addPlatform(SocialMediaPlatform smp){_myfeeds.add(smp);}
    public void update(Feed f, String s){
        for (int i=0; i<_myfeeds.size(); i++){
            SocialMediaPlatform smp = _myfeeds.get(i);
            if (smp.getName().equals(s))
                _myfeeds.get(i).addFeed(f);
        }
    }
    public String toString(){
        String s = "";
        for (SocialMediaPlatform smp : _myfeeds) {
            for (int i=0; i<smp.size(); i++){
                Feed f = smp.getFeed(i);
                s = s + f.getTitle() + " " + f.getDesc() + "\n";
            }
        }
        return s;
    }
}

/**********************************************************/
/* Factory Pattern Interface/Classes                      */
/**********************************************************/

// Factory Creator Interface
interface SMPFactory {
    SocialMediaPlatform createPlatform();
}

// Concrete Factory classes for each platform
class InstagramFactory implements SMPFactory {
    public SocialMediaPlatform createPlatform() {
        return new Instagram();
    }
}

class FacebookFactory implements SMPFactory {
    public SocialMediaPlatform createPlatform(){
        return new Facebook();
    }
}

class TwitterFactory implements SMPFactory{
    public SocialMediaPlatform createPlatform(){
        return new Twitter();
    }
}


public class Main {
    public static void main(String[] args) throws IOException {
        // Create main newsfeed from file
        NewsFeed nf = new NewsFeed("data.txt");

        // Create SMP factories
        SMPFactory instagramFactory = new InstagramFactory();
        SMPFactory facebookFactory = new FacebookFactory();
        SMPFactory twitterFactory = new TwitterFactory();


        // Create the platforms container and add SMPs
        ArrayList<SocialMediaPlatform> platforms = new ArrayList<>();
        platforms.add(instagramFactory.createPlatform());
        platforms.add(facebookFactory.createPlatform());
        platforms.add(twitterFactory.createPlatform());

        // Create Users and subscribe
        User user1 = new User("Alice");
        User user2 = new User("Chris");
        User user3 = new User("Aaron");
        
        for(int i = 0; i < platforms.size(); i++){
            user1.addPlatform(platforms.get(i));
            user2.addPlatform(platforms.get(i));
            user3.addPlatform(platforms.get(i));
            platforms.get(i).subscribe(user1);
            platforms.get(i).subscribe(user2);
            platforms.get(i).subscribe(user3);
        }
        // Run a simulation to generate random feeds for the SMPs
        for(int i = 0; i < 20; i++){
            platforms.get(0).generateFeed(nf);
            platforms.get(1).generateFeed(nf);
            platforms.get(2).generateFeed(nf);
        }

        // Perform analysis
        AnalysisBehavior ab = new CountAllAnalysis();
        System.out.println(platforms.get(0).analyzeFeed("guess", new CountAllAnalysis()));

        // Print Users' Contents

    }

}
