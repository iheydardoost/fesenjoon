package fsjMessaging;

import fsjAccount.User;

import java.time.LocalDateTime;

public class Tweet extends Message{
    private long tweetID;
    private long lastCommentID;

    public Tweet() {
    }

    public Tweet(String msgText, LocalDateTime msgDateTime, User writer) {
        super(msgText, msgDateTime, writer.getUserID());
        this.tweetID = generateID(writer);
        this.lastCommentID = 0;
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

    private long generateID(User writer){
        writer.setLastTweetID(writer.getLastTweetID()+1);
        return writer.getLastTweetID();
    }
}
