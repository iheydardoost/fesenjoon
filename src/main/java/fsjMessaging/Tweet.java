package fsjMessaging;

import fsjAccount.User;
import fsjCLI.ConsoleColors;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tweet extends Message{
    private long tweetID;
    private long lastCommentID;
    public ArrayList<Long> likeList;

    public Tweet() {
    }

    public Tweet(String msgText, LocalDateTime msgDateTime, long userID, long tweetID) {
        super(msgText, msgDateTime, userID);
        this.tweetID = tweetID;
        this.lastCommentID = 0;
        this.likeList = new ArrayList<>();
    }

    public long getTweetID() {
        return tweetID;
    }

    public void setTweetID(long tweetID) {
        this.tweetID = tweetID;
    }

    public long getLastCommentID() {
        return lastCommentID;
    }

    public void setLastCommentID(long lastCommentID) {
        this.lastCommentID = lastCommentID;
    }

    public static long generateTweetID(User writer){
        writer.setLastTweetID(writer.getLastTweetID()+1);
        return writer.getLastTweetID();
    }

    public static long showTweet(long userID,long tweetID){
        Tweet tweet = loadTweet(userID,tweetID);
        Tweet.printTweet(tweet);
        return tweet.getLastCommentID();
    }

    public static Tweet loadTweet(long userID, long tweetID){
        String path = ".\\src\\main\\ServerData\\Tweets\\" + Long.toHexString(userID) + "\\" + Long.toHexString(tweetID) + ".json";
        File file = new File(path);
        if(file.isFile()){
            try {
                Tweet tweet = JsonHandler.mapper.readValue(file,Tweet.class);
                LogHandler.logger.info("tweet "+tweetID+" from user "+userID+" loaded.");
                return tweet;
            } catch (IOException e) {
                //e.printStackTrace();
                LogHandler.logger.error("tweet "+tweetID+" from user "+userID+" could not be read.");
            }
        }
        return null;
    }

    public static void printTweet(Tweet tweet){
        String userName = User.findUserName(tweet.getUserID());
        if(userName==null){
            LogHandler.logger.error("user "+tweet.getUserID()+" userName could not found.");
            return;
        }
        System.out.print(ConsoleColors.PURPLE);
        System.out.println("\n\n==========Tweet==========");
        System.out.println(tweet.getMsgText());
        System.out.println("Writer: " +userName);
        System.out.println("Date: " +tweet.getMsgDateTime().toLocalDate().toString());
        System.out.println("Time: " +tweet.getMsgDateTime().toLocalTime().toString());
        System.out.println("TweetID: " +tweet.getTweetID());
        System.out.println("number of comments: " +tweet.getLastCommentID());
        System.out.print(ConsoleColors.COLOR_RESET);
    }

    public void saveTweet() throws Exception{
        String path = ".\\src\\main\\ServerData\\Tweets\\" + Long.toHexString(this.getUserID());
        File file1 = new File(path);
        if(!file1.isDirectory()) file1.mkdir();
        path = path + "\\" + Long.toHexString(this.getTweetID()) + ".json";
        File file2 = new File(path);
        JsonHandler.mapper.writeValue(file2,this);
    }

}
