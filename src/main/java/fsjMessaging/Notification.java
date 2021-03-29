package fsjMessaging;

import java.time.LocalDateTime;

public class Notification extends Message{
    private boolean seen;

    public Notification() {
    }

    public Notification(String msgText, LocalDateTime msgDateTime, long userID) {
        super(msgText, msgDateTime, userID);
        this.seen = false;
    }

}
