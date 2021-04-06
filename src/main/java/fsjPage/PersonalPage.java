package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjLogger.LogHandler;
import fsjMain.Main;
import fsjMessaging.Comment;
import fsjMessaging.Notification;
import fsjMessaging.Tweet;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PersonalPage {
    private static final Page personalPage = new Page("Personal Page", new CommandParser(), new ArrayList<>());
    private static final Page myTweetsPage = new Page("My Tweets",new CommandParser(), new ArrayList<>());
    private static final Page listPage = new Page("List Page",new CommandParser(),new ArrayList<>());
    private static final Page visitPage = new Page("Visit User Page",new CommandParser(),new ArrayList<>());
    enum LIST{FOLLOWINGS,FOLLOWERS,BLACK_LIST}

    public PersonalPage() {
    }

    public static void initPersonalPage() {
        personalPage.addChoice("new --tweet").addChoice("edit --info <XXX>:\n\tfirstName\n\tlastName\n\tphoneNumber\n\tbio");
        personalPage.addChoice("show --info");
        personalPage.addChoice("goto --list <XXX>:\n\tmyTweets\n\tfollowings\n\tfollowers\n\tblackList\n\tnotifications");
        personalPage.getCommandParser().addValidCommand("new").addValidCommand("show").addValidCommand("goto");
        personalPage.getCommandParser().addValidCommand("exit").addValidCommand("edit").addValidCommand("list").addValidCommand("return");
        personalPage.getCommandParser().addValidTag("--tweet").addValidTag("--list").addValidTag("--info");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        myTweetsPage.addChoice("goto --tweet <TweetID>").addChoice("new --comment");
        myTweetsPage.getCommandParser().addValidCommand("goto").addValidCommand("return").addValidCommand("exit").addValidCommand("list");
        myTweetsPage.getCommandParser().addValidCommand("new").addValidTag("--tweet").addValidTag("--comment");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        listPage.addChoice("goto --user <userName>").addChoice("show");
        listPage.getCommandParser().addValidCommand("exit").addValidCommand("return").addValidCommand("goto");
        listPage.getCommandParser().addValidCommand("list").addValidCommand("show").addValidTag("--user");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        visitPage.addChoice("follow").addChoice("message").addChoice("report").addChoice("block");
        visitPage.getCommandParser().addValidCommand("return").addValidCommand("exit").addValidCommand("follow").
                addValidCommand("message").addValidCommand("report").addValidCommand("block");
    }

    public static FsjPageManager.CompleteState personalPageManager() {
        FsjPageManager.CompleteState state = FsjPageManager.CompleteState.NONE;

        while (true) {
            personalPage.printChoiceList();
            if (personalPage.getCommandParser().listenToUser()) {
                switch (personalPage.getCommandParser().getCommand()) {
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    case "new":
                        if (personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--tweet")) {
                            personalPage.getCommandParser().improperInput(true, "");
                            break;
                        }
                        state = newTweetCommand(personalPage);
                        break;
                    case "show":
                        state = showCommand();
                        break;
                    case "goto":
                        state = gotoCommand();
                        break;
                    case "list":
                        personalPage.printChoiceList();
                        break;
                    case "edit":
                        state = editCommand();
                        break;
                    default:
                        personalPage.getCommandParser().improperInput(false, "choose a valid command.");
                        state = FsjPageManager.CompleteState.NONE;
                        break;
                }
            }

            if (state == FsjPageManager.CompleteState.EXIT) return state;
        }
    }

    private static FsjPageManager.CompleteState newTweetCommand(Page page) {
        String msg = "";
        System.out.print("type your tweet\n(type \"--end\" in one line to end)\n(type \"--quit\" to cancel)\n:");
        while(true) {
            page.getCommandParser().listen();
            if(page.getCommandParser().inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
            if(page.getCommandParser().inStr.equals("--end")) break;
            msg += page.getCommandParser().inStr;
            msg += "\n";
        }

        Tweet tweet = new Tweet(msg,LocalDateTime.now(), Main.mainUser.getUserID(),Tweet.generateTweetID(Main.mainUser));
        try {
            tweet.saveTweet();
        } catch (Exception e) {
            //e.printStackTrace();
            LogHandler.logger.error("tweet " + tweet.getTweetID() + " from user " + tweet.getUserID() + " could not be saved.");
            return FsjPageManager.CompleteState.NONE;
        }

        System.out.println("your tweet is successfully saved.");
        LogHandler.logger.info("user " + tweet.getUserID() + " tweeted " + tweet.getTweetID());
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState showCommand() {
        if (personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--info")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }
        Main.mainUser.printUserInfo();
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState editCommand() {
        if (personalPage.getCommandParser().getArgs().size() != 1 ||
                personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--info")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }

        String inStr;
        switch (personalPage.getCommandParser().getArgs().get(0)){
            case "firstName":
                System.out.print("enter firstName :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.length()==0) {
                    personalPage.getCommandParser().improperInput(false, "can not be empty.");
                    break;
                }
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setFirstName(inStr);
                System.out.println("firstName changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.getUserID() + " changed firstName.");
                break;
            case "lastName":
                System.out.print("enter lastName :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.length()==0) {
                    personalPage.getCommandParser().improperInput(false, "can not be empty.");
                    break;
                }
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setLastName(inStr);
                System.out.println("lastName changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.getUserID() + " changed lastName.");
                break;
            case "phoneNumber":
                System.out.print("enter phoneNumber :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setPhoneNumber(inStr);
                System.out.println("phoneNumber changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.getUserID() + " changed phoneNumber.");
                break;
            case "bio":
                System.out.print("enter bio :");
                personalPage.getCommandParser().listen();
                inStr = personalPage.getCommandParser().inStr;
                if(inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
                Main.mainUser.setBio(inStr);
                System.out.println("bio changed successfully.");
                LogHandler.logger.info("user " + Main.mainUser.getUserID() + " changed bio.");
                break;
            default:
                personalPage.getCommandParser().improperInput(false, "can not change.");
                break;
        }

        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState gotoCommand() {
        if (personalPage.getCommandParser().getArgs().size() != 1 ||
                personalPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--list")) {
            personalPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }

        switch (personalPage.getCommandParser().getArgs().get(0)){
            case "myTweets":
                return myTweetsManager();
            case "followings":
                return listsManager(LIST.FOLLOWINGS);
            case "followers":
                return listsManager(LIST.FOLLOWERS);
            case "blackList":
                return listsManager(LIST.BLACK_LIST);
            case "notifications":
                return notificationManager();
            default:
                personalPage.getCommandParser().improperInput(true,"");
                break;
        }
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState notificationManager(){
        Main.mainUser.printNotifications();
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState listsManager(LIST list){
        ArrayList<Long> arrayList;
        String header="";
        switch (list){
            case FOLLOWINGS:
                arrayList = Main.mainUser.getFollowings();
                header = "**********FOLLOWINGS**********";
                break;
            case FOLLOWERS:
                arrayList = Main.mainUser.getFollowers();
                header = "**********FOLLOWERS**********";
                break;
            case BLACK_LIST:
                arrayList = Main.mainUser.getBlackList();
                header = "**********BLACK_LIST**********";
                break;
            default:
                return FsjPageManager.CompleteState.NONE;
        }

        System.out.println(header);
        for(int i=0;i<arrayList.size();i++){
            System.out.println(User.findUserName(arrayList.get(i)));
        }

        while (true) {
            listPage.printChoiceList();
            if (listPage.getCommandParser().listenToUser()) {
                switch (listPage.getCommandParser().getCommand()) {
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    case "goto":
                        if (listPage.getCommandParser().getArgs().size() != 1 ||
                                listPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--user")) {
                            listPage.getCommandParser().improperInput(true, "");
                            return FsjPageManager.CompleteState.NONE;
                        }
                        return visitAnotherUser(User.userNameList.get(listPage.getCommandParser().getArgs().get(0)));
                    case "list":
                        break;
                    case "show":
                        System.out.println(header);
                        for(int i=0;i<arrayList.size();i++){
                            System.out.println(User.findUserName(arrayList.get(i)));
                        }
                        break;
                    default:
                        listPage.getCommandParser().improperInput(true,"");
                        break;
                }
            }
        }
    }

    public static FsjPageManager.CompleteState visitAnotherUser(long userID){
        User user = User.loadUser(userID);
        if(user.getAccountStatus()== User.AccountStatus.INACTIVE)
            return FsjPageManager.CompleteState.USER_INACTIVE;

        System.out.println("#################### User Personal Page ########################");
        System.out.println("firstName = "+user.getFirstName());
        System.out.println("lastName = "+user.getLastName());
        System.out.println("userName = "+user.getUserName());
        boolean showLastSeen = false;
        switch (user.getLastSeenVisibility()){
            case EVERYONE:
                showLastSeen=true;
                break;
            case FOLLOWINGS:
                if(user.getFollowings().contains(Main.mainUser.getUserID())) showLastSeen=true;
                break;
            case NO_ONE:
                showLastSeen=false;
                break;
            default:
                break;
        }
        if(showLastSeen){
            switch (user.getLastSeenStatus()){
                case LAST_SEEN_RECENTLY:
                    System.out.println("lastSeen = "+User.LastSeenStatus.LAST_SEEN_RECENTLY);
                    break;
                case LAST_SEEN_DATE:
                    System.out.println("lastSeen = "+user.lastSeen.toString());
                    break;
                default:
                    break;
            }
        }
        if(Main.mainUser.getFollowings().contains(userID))
            System.out.println("you have followed this user.");
        else
            System.out.println("you have not followed this user.");

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        while(true){
            visitPage.printChoiceList();
            if(visitPage.getCommandParser().listenToUser()){
                switch (visitPage.getCommandParser().getCommand()){
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "follow":
                        if(user.getBlackList().contains(Main.mainUser.getUserID())){
                            System.out.println("this user blocked you.");
                            break;
                        }
                        if(Main.mainUser.getBlackList().contains(userID)){
                            System.out.println("you blocked this user.");
                            break;
                        }
                        if(user.getPrivacyStatus()== User.PrivacyStatus.PUBLIC){
                            Main.mainUser.getFollowings().add(userID);
                            user.getFollowers().add(Main.mainUser.getUserID());
                            user.getNotificationList().add(new Notification("user "+Main.mainUser.getUserName()+" followed you.",
                                                            LocalDateTime.now(),userID, Notification.NOTIFICATION_CONTEX.INFO));
                            System.out.println("followed successfully.");
                            break;
                        }
                        else if(user.getPrivacyStatus()== User.PrivacyStatus.PRIVATE){
                            user.getNotificationList().add(new Notification("user "+Main.mainUser.getUserName()+" wants to follow you.",
                                    LocalDateTime.now(),userID, Notification.NOTIFICATION_CONTEX.OTHERS_REQUEST));
                            Main.mainUser.getNotificationList().add(new Notification("user "+user.getUserName()+" follow request.",
                                    LocalDateTime.now(),userID, Notification.NOTIFICATION_CONTEX.MY_REQUEST));
                            break;
                        }
                        break;
                    case "report":
                        Main.report("user "+user.getUserID());
                        break;
                    case "block":
                        Main.mainUser.blackList.add(user.getUserID());
                        break;
                    case "message":
                        if(!user.getFollowers().contains(Main.mainUser.getUserID())) {
                            System.out.println("you did not followed this user yet.");
                            break;
                        }
                        System.out.println("not implemented yet.");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static FsjPageManager.CompleteState myTweetsManager(){
        long tweetNum = Main.mainUser.getLastTweetID();
        if(tweetNum==0){
            System.out.println("you did not write any tweet yet.");
            return FsjPageManager.CompleteState.NONE;
        }
        for(long i=1;i<=tweetNum;i++){
            Tweet.showTweet(Main.mainUser.getUserID(),i);
        }

        long commentNum=0;
        long tweetID;

        while (true){
            myTweetsPage.printChoiceList();
            if(myTweetsPage.getCommandParser().listenToUser()){
                switch (myTweetsPage.getCommandParser().getCommand()){
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    case "list":
                        break;
                    case "show":
                        if (myTweetsPage.getCommandParser().getArgs().size() != 1 ||
                                myTweetsPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--tweet")) {
                            myTweetsPage.getCommandParser().improperInput(true, "");
                            return FsjPageManager.CompleteState.NONE;
                        }
                        tweetID = Long.parseLong(myTweetsPage.getCommandParser().getArgs().get(0));
                        commentNum = Tweet.showTweet(Main.mainUser.getUserID(),tweetID);
                        if(commentNum==0){
                            System.out.println("this tweet does not have any comment yet.");
                            break;
                        }
                        for(long i=1;i<=commentNum;i++){
                            Comment.showComment(Main.mainUser.getUserID(),tweetID,i);
                        }
                        break;
                    case "new":
                        if (myTweetsPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--comment")) {
                            myTweetsPage.getCommandParser().improperInput(true, "");
                            break;
                        }
                        System.out.print("enter tweetID you want to add comment :");
                        myTweetsPage.getCommandParser().listen();
                        if(myTweetsPage.getCommandParser().inStr.length()==0){
                            myTweetsPage.getCommandParser().improperInput(false, "can not be empty");
                            break;
                        }
                        return Comment.newCommentCommand(myTweetsPage,Long.parseLong(myTweetsPage.getCommandParser().inStr));
                    default:
                        myTweetsPage.getCommandParser().improperInput(true,"");
                        break;
                }
            }
        }

    }

}
