package fsjMessaging;

import java.time.LocalDateTime;

public class Comment extends Tweet{
    private int commentID;

    public Comment() {
    }

    public Comment(String msgText, LocalDateTime msgDateTime, long userID, int tweetNum, int commentID) {
        super(msgText, msgDateTime, userID, tweetNum);
        this.commentID = commentID;
    }
}
