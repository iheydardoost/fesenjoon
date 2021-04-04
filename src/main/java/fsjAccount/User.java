package fsjAccount;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;
import fsjMain.Main;
import fsjMessaging.Notification;
import fsjMessaging.Tweet;
import fsjPage.FsjPageManager;
import fsjPage.MessageRoom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class User {

    private long userID;
    public String userName;
    public String firstName;
    public String lastName;
    private int passwordHash;
    private int passwordLength;
    public LocalDate dateOfBirth;
    public String email;
    public String phoneNumber;
    public String bio;
    public LocalDateTime lastSeen;
    public AccountStatus accountStatus;
    public LastSeenStatus lastSeenStatus;
    public LastSeenVisibility lastSeenVisibility;
    public PrivacyStatus privacyStatus;
    public ArrayList<MessageRoom> messageRoomList;
    public ArrayList<Long> followers;
    public ArrayList<Long> followings;
    public ArrayList<Long> blackList;
    public ArrayList<Long> silentList;
    public ArrayList<Notification> notificationList;
    private static long lastUserID = Long.MAX_VALUE;
    private long lastTweetID;
    public static Hashtable<String, Long> userNameList = new Hashtable<>();
    public static Hashtable<String, Long> emailList = new Hashtable<>();

    public enum AccountStatus {ACTIVE, INACTIVE}

    public enum LastSeenStatus {LAST_SEEN_RECENTLY, LAST_SEEN_DATE}

    public enum LastSeenVisibility {EVERYONE, NO_ONE, FOLLOWINGS}

    public enum PrivacyStatus {PRIVATE, PUBLIC}

    public User() {
    }

    private User(String userName, String firstName, String lastName, int passwordHash, int passwordLength,
                 LocalDate dateOfBirth, String email, String phoneNumber, String bio) {
        this.userID = generateID();
        this.userName = userName;
        userNameList.put(userName, userID);
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.passwordLength = passwordLength;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        emailList.put(email, userID);
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.accountStatus = AccountStatus.ACTIVE;
        this.lastSeen = LocalDateTime.now();
        this.lastSeenStatus = LastSeenStatus.LAST_SEEN_RECENTLY;
        this.lastSeenVisibility = LastSeenVisibility.EVERYONE;
        this.privacyStatus = PrivacyStatus.PUBLIC;
        this.messageRoomList = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.blackList = new ArrayList<>();
        this.silentList = new ArrayList<>();
        this.notificationList = new ArrayList<>();
        this.lastTweetID = 0;
        saveToUserList();
    }

    public static class UserBuilder {
        String userName;
        String firstName;
        String lastName;
        int passwordHash;
        int passwordLength;
        LocalDate dateOfBirth;
        String email;
        String phoneNumber;
        String bio;

        public UserBuilder() {
        }

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

        public User build() {
            return new User(this.userName, this.firstName, this.lastName, this.passwordHash, this.passwordLength, this.dateOfBirth,
                    this.email, this.phoneNumber, this.bio);
        }
    }

    public static void initUser() throws Exception {
        String path;
        path = ".\\src\\main\\ServerData\\UserList\\lastUserID.json";
        File file1 = new File(path);
        if(file1.isFile()) lastUserID = JsonHandler.mapper.readValue(file1, Long.class);
        path = ".\\src\\main\\ServerData\\UserList\\userNameList.json";
        File file2 = new File(path);
        if(file2.isFile()) userNameList = JsonHandler.mapper.readValue(file2, new TypeReference<Hashtable<String, Long>>() {});
        path = ".\\src\\main\\ServerData\\UserList\\emailList.json";
        File file3 = new File(path);
        if(file3.isFile()) emailList = JsonHandler.mapper.readValue(file3, new TypeReference<Hashtable<String, Long>>() {});
    }

    public static void saveUserClass() throws Exception{
        String path;
        path = ".\\src\\main\\ServerData\\UserList\\lastUserID.json";
        JsonHandler.mapper.writeValue(new File(path), lastUserID);
        path = ".\\src\\main\\ServerData\\UserList\\userNameList.json";
        JsonHandler.mapper.writeValue(new File(path), userNameList);
        path = ".\\src\\main\\ServerData\\UserList\\emailList.json";
        JsonHandler.mapper.writeValue(new File(path), emailList);
    }

    public void saveToUserList() {
        this.lastSeen = LocalDateTime.now();
        File file = new File(".\\src\\main\\ServerData\\UserList\\" + Long.toHexString(this.userID) + ".json");
        try {
            JsonHandler.mapper.writeValue(file, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPassCorrect(String password) {
        if (password.length() != this.passwordLength) return false;
        if (password.hashCode() != this.passwordHash) return false;
        return true;
    }

    private long generateID() {
        return (--lastUserID);
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(int passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LastSeenStatus getLastSeenStatus() {
        return lastSeenStatus;
    }

    public void setLastSeenStatus(LastSeenStatus lastSeenStatus) {
        this.lastSeenStatus = lastSeenStatus;
    }

    public LastSeenVisibility getLastSeenVisibility() {
        return lastSeenVisibility;
    }

    public void setLastSeenVisibility(LastSeenVisibility lastSeenVisibility) {
        this.lastSeenVisibility = lastSeenVisibility;
    }

    public PrivacyStatus getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(PrivacyStatus privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public ArrayList<MessageRoom> getMessageRoomList() {
        return messageRoomList;
    }

    public void setMessageRoomList(ArrayList<MessageRoom> messageRoomList) {
        this.messageRoomList = messageRoomList;
    }

    public ArrayList<Long> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Long> followers) {
        this.followers = followers;
    }

    public ArrayList<Long> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<Long> followings) {
        this.followings = followings;
    }

    public ArrayList<Long> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<Long> blackList) {
        this.blackList = blackList;
    }

    public ArrayList<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ArrayList<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public static long getLastUserID() {
        return lastUserID;
    }

    public static void setLastUserID(long lastUserID) {
        User.lastUserID = lastUserID;
    }

    public long getLastTweetID() {
        return lastTweetID;
    }

    public void setLastTweetID(long lastTweetID) {
        this.lastTweetID = lastTweetID;
    }

    public static Hashtable<String, Long> getUserNameList() {
        return userNameList;
    }

    public static void setUserNameList(Hashtable<String, Long> userNameList) {
        User.userNameList = userNameList;
    }

    public static Hashtable<String, Long> getEmailList() {
        return emailList;
    }

    public static void setEmailList(Hashtable<String, Long> emailList) {
        User.emailList = emailList;
    }

    public void printUserInfo(){
        System.out.println("userName = " + this.userName);
        System.out.println("firstName =" + this.firstName);
        System.out.println("lastName =" + this.lastName);
        System.out.println("dateOfBirth =" + this.dateOfBirth);
        System.out.println("email = " + this.email);
        System.out.println("phoneNumber = " + this.phoneNumber);
        System.out.println("bio = " + this.bio);
    }

    public static String findUserName(long userID){
        for(Object o: userNameList.entrySet()){
            Map.Entry entry = (Map.Entry) o;
            if(entry.getKey().equals(userID))
                return (String) entry.getValue();
        }
        return null;
    }

    public static User loadUser(long userID){
        String path = ".\\src\\main\\ServerData\\UserList\\" + Long.toHexString(userID) + ".json";
        File file = new File(path);
        if(file.isFile()){
            try {
                User user = JsonHandler.mapper.readValue(file,User.class);
                LogHandler.logger.info("user " + userID + " loaded.");
                return user;
            } catch (IOException e) {
                //e.printStackTrace();
                LogHandler.logger.error("user "+userID+" could not be read.");
            }
        }
        return null;
    }
}

