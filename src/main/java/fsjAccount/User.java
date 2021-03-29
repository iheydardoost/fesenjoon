package fsjAccount;

import fsjMessaging.Notification;
import fsjPage.MessageRoom;

import java.time.LocalDate;
import java.util.ArrayList;

public class User {

    private final long userID;
    public final String userName;
    public String firstName;
    public String lastName;
    private int passwordHash;
    private int passwordLength;
    public LocalDate dateOfBirth;
    public String email;
    public String phoneNumber;
    public String bio;
    public AccountStatus accountStatus;
    public LastSeenStatus lastSeenStatus;
    public LastSeenVisibility lastSeenVisibility;
    public PrivacyStatus privacyStatus;
    public ArrayList<MessageRoom> messageRoomList;
    public ArrayList<User> followers;
    public ArrayList<User> followings;
    public ArrayList<User> blackList;
    public ArrayList<Notification> notificationList;
    private static long lastUserID = Long.MAX_VALUE;
    private int lastTweetNum;

    enum AccountStatus {ACTIVE, INACTIVE}

    enum LastSeenStatus {LAST_SEEN_RECENTLY, LAST_SEEN_DATE}

    enum LastSeenVisibility {EVERYONE, NO_ONE, FOLLOWINGS}

    enum PrivacyStatus {PRIVATE, PUBLIC}

    private User(String userName, String firstName, String lastName, int passwordHash, int passwordLength,
                LocalDate dateOfBirth, String email, String phoneNumber, String bio) {
        this.userID = generateID();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.passwordLength = passwordLength;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.accountStatus = AccountStatus.ACTIVE;
        this.lastSeenStatus = LastSeenStatus.LAST_SEEN_RECENTLY;
        this.lastSeenVisibility = LastSeenVisibility.EVERYONE;
        this.privacyStatus = PrivacyStatus.PUBLIC;
        this.messageRoomList = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.blackList = new ArrayList<>();
        this.notificationList = new ArrayList<>();
        this.lastTweetNum = 0;
    }

    public class UserBuilder {
        String userName;
        String firstName;
        String lastName;
        int passwordHash;
        int passwordLength;
        LocalDate dateOfBirth;
        String email;
        String phoneNumber;
        String bio;

        public UserBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.passwordLength = password.length();
            this.passwordHash = password.hashCode();
            return this;
        }

        public UserBuilder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder setBio(String bio) {
            this.bio = bio;
            return this;
        }

        public User build(){
            return new User(this.userName,this.firstName,this.lastName,this.passwordHash,this.passwordLength,this.dateOfBirth,
                            this.email,this.phoneNumber,this.bio);
        }
    }

    private long generateID(){
        return (--lastUserID);
    }

    public int getLastTweetNum() {
        return lastTweetNum;
    }

    public void setLastTweetNum(int lastTweetNum) {
        this.lastTweetNum = lastTweetNum;
    }

}

