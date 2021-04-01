package fsjMessaging;

import java.time.LocalDateTime;

public class Tweet extends Message{
    private long tweetID;
    private long lastTweetID = Long.MIN_VALUE;

    public Tweet() {
    }

    public Tweet(String msgText, LocalDateTime msgDateTime, long userID, long tweetID) {
        super(msgText, msgDateTime, userID);
        this.tweetID = tweetID;
    }

    public long generateID(){
        return (++lastTweetID);
    }
}
