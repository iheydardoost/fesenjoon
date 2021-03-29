package fsjMessaging;

import java.util.ArrayList;

public class NotificationList {
    private ArrayList<Notification> notifList;

    public NotificationList() {
        this.notifList = new ArrayList<>();
    }

    public void add(Notification notification){
        notifList.add(notification);
    }

}
