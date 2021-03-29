package fsjMessaging;

import java.time.LocalDateTime;

public class Tweet extends Message{
    private int tweetNum;

    public Tweet() {
    }

    public Tweet(String msgText, LocalDateTime msgDateTime, long userID, int tweetNum) {
        super(msgText, msgDateTime, userID);
        this.tweetNum = tweetNum;
    }
}
