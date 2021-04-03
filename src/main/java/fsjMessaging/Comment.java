package fsjMessaging;

import fsjAccount.User;

import java.time.LocalDateTime;

public class Comment extends Tweet{
    private long commentID;
    private long lastCommentID = Long.MIN_VALUE;

    public Comment() {
    }

    public Comment(String msgText, LocalDateTime msgDateTime, User writer) {
        super(msgText, msgDateTime, writer);
        this.commentID = commentID;
    }

    public long generateID(){
        return (++lastCommentID);
    }
}
