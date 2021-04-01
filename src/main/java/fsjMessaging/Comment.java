package fsjMessaging;

import java.time.LocalDateTime;

public class Comment extends Tweet{
    private long commentID;
    private long lastCommentID = Long.MIN_VALUE;

    public Comment() {
    }

    public Comment(String msgText, LocalDateTime msgDateTime, long userID, int tweetID, long commentID) {
        super(msgText, msgDateTime, userID, tweetID);
        this.commentID = commentID;
    }

    public long generateID(){
        return (++lastCommentID);
    }
}
