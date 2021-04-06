package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjLogger.LogHandler;
import fsjMain.Main;
import fsjMessaging.Comment;
import fsjMessaging.Tweet;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ExplorerPage{
    private static final Page explorer = new Page("Explorer",new CommandParser(),new ArrayList<>());
    private static final Page commentList = new Page("Comment List",new CommandParser(),new ArrayList<>());
    private static Tweet shownTweet = null;
    private static Comment shownComment = null;
    private static ArrayList<Long> shuffledUsers = new ArrayList<>();
    private static long shownTweetOffset = 1;
    private static int shuffledUsersOffset = 0;
    private static long shownCommentOffset=1;

    public static void initExplorer(){
        explorer.addChoice("move --next").addChoice("move --previous").addChoice("like").addChoice("addto --saved");
        explorer.addChoice("retweet").addChoice("forward").addChoice("block").addChoice("silent").addChoice("report");
        explorer.addChoice("goto --user").addChoice("goto --comment").addChoice("new --comment").addChoice("search --user <UserName>");
        explorer.getCommandParser().addValidCommand("exit").addValidCommand("return").addValidCommand("move").addValidTag("--next").
                addValidTag("--previous").addValidCommand("like").addValidCommand("addto").addValidTag("--saved").
                addValidCommand("retweet").addValidCommand("forward").addValidCommand("block").addValidCommand("silent").
                addValidCommand("report").addValidCommand("goto").addValidTag("--user").addValidTag("--comment").addValidCommand("new").
                addValidCommand("list").addValidCommand("search");
        ////////////////////////////////////////////////////////////////////////////////
        commentList.addChoice("move --next").addChoice("move --previous").addChoice("like").addChoice("addto --saved");
        commentList.addChoice("forward").addChoice("block").addChoice("silent").addChoice("report");
        commentList.addChoice("goto --user");
        commentList.getCommandParser().addValidCommand("exit").addValidCommand("return").addValidCommand("move").addValidTag("--next").
                addValidTag("--previous").addValidCommand("like").addValidCommand("addto").addValidTag("--saved").
                addValidCommand("forward").addValidCommand("block").addValidCommand("silent").addValidCommand("report").
                addValidCommand("goto").addValidTag("--user");
    }

    public static FsjPageManager.CompleteState explorerPageManager(){
        FsjPageManager.CompleteState state = FsjPageManager.CompleteState.NONE;
        explorer.printChoiceList();
        if(ExplorerPage.chooseShownTweet()== ExplorerPage.SHOWN_TWEET_STATUS.EMPTY)
            return FsjPageManager.CompleteState.NONE;

        while(true){
            if(explorer.getCommandParser().listenToUser()){
                switch (explorer.getCommandParser().getCommand()){
                    case "move":
                        if (explorer.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--next")) {
                            ExplorerPage.moveTweet(1);
                        }
                        else if(explorer.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--previous")){
                            ExplorerPage.moveTweet(-1);
                        }
                        else{
                            explorer.getCommandParser().improperInput(true, "");
                        }
                        break;
                    case "like":
                        shownTweet.likeList.add(Main.mainUser.getUserID());
                        System.out.println("liked successfully.");
                        break;
                    case "addto":
                        if (explorer.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--saved")) {
                            explorer.getCommandParser().improperInput(true, "");
                            break;
                        }
                        break;
                    case "retweet":
                        ExplorerPage.retweet();
                        break;
                    case "forward":
                        break;
                    case "block":
                        Main.mainUser.blackList.add(shownTweet.getUserID());
                        break;
                    case "silent":
                        Main.mainUser.silentList.add(shownTweet.getUserID());
                        break;
                    case "report":
                        Main.report("user "+shownTweet.getUserID()+" ,tweet "+shownTweet.getTweetID());
                        break;
                    case "goto":
                        if (explorer.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--user")) {
                            state = PersonalPage.visitAnotherUser(shownTweet.getUserID());
                        }
                        else if(explorer.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--comment")){
                            state = commentListManager();
                        }
                        else{
                            explorer.getCommandParser().improperInput(true, "");
                            break;
                        }
                    case "new":
                        if (explorer.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--comment")) {
                            explorer.getCommandParser().improperInput(true, "");
                            break;
                        }
                        state = Comment.newCommentCommand(explorer,shownTweet.getTweetID());
                    case "search":
                        if (explorer.getCommandParser().getArgs().size() != 1 ||
                                explorer.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--user")) {
                            explorer.getCommandParser().improperInput(true, "");
                            break;
                        }
                        state = searchUserCommand();
                        break;
                    case "list":
                        explorer.printChoiceList();
                        break;
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    default:
                        break;
                }

                if(state== FsjPageManager.CompleteState.EXIT) return state;
            }
        }
    }

    private static FsjPageManager.CompleteState searchUserCommand(){
        if(User.userNameList.containsKey(explorer.getCommandParser().getArgs().get(0)))
            return PersonalPage.visitAnotherUser(shownTweet.getUserID());
        else {
            System.out.println("UserName <" + explorer.getCommandParser().getArgs().get(0) + "> not found.");
            return FsjPageManager.CompleteState.NONE;
        }
    }

    private enum SHOWN_TWEET_STATUS{EMPTY,NORMAL}

    private static ExplorerPage.SHOWN_TWEET_STATUS chooseShownTweet(){
        shuffledUsers.clear();
        shuffledUsersOffset=0;
        long usersNumber;
        if(User.userNameList.size()<100){
            if(User.userNameList.size()==0) {
                System.out.println("there is no user yet.");
                return SHOWN_TWEET_STATUS.EMPTY;
            }
            usersNumber=User.userNameList.size();
        }
        else usersNumber=100;

        long rndNum=0;
        for(int i=0;i<usersNumber;i++){
            rndNum = (Long.MAX_VALUE-1) + (long) (Math.random()*((Long.MAX_VALUE-User.userNameList.size())-(Long.MAX_VALUE-1)));
            shuffledUsers.add(rndNum);
        }

        User user = User.loadUser(shuffledUsers.get(shuffledUsersOffset));
        long lastTweetID = user.getLastTweetID();
        User.AccountStatus accStatus = user.accountStatus;
        User.PrivacyStatus prvStatus = user.privacyStatus;
        while(lastTweetID==0 || accStatus==User.AccountStatus.INACTIVE || prvStatus==User.PrivacyStatus.PRIVATE ||
                Main.mainUser.blackList.contains(user.getUserID()) || Main.mainUser.silentList.contains(user.getUserID())){
            if(shuffledUsersOffset==shuffledUsers.size())
                shuffledUsersOffset = 0;
            shuffledUsersOffset++;
            lastTweetID = User.loadUser(shuffledUsers.get(shuffledUsersOffset)).getLastTweetID();
        }
        shownTweetOffset= 1 + (long) (Math.random() * (lastTweetID - 1));
        shownTweet = Tweet.loadTweet(shuffledUsers.get(shuffledUsersOffset),shownTweetOffset);
        Tweet.printTweet(shownTweet);
        return ExplorerPage.SHOWN_TWEET_STATUS.NORMAL;
    }

    private static void moveTweet(int step){
        long lastTweetID=0;
        User.AccountStatus accStatus;
        User.PrivacyStatus prvStatus;
        while(true) {
            if (step==1) {
                if (shuffledUsersOffset == shuffledUsers.size())
                    shuffledUsersOffset = 0;
                shuffledUsersOffset++;
            } else if (step==-1) {
                if (shuffledUsersOffset == 0)
                    shuffledUsersOffset = shuffledUsers.size();
                shuffledUsersOffset--;
            } else
                return;

            User user = User.loadUser(shuffledUsers.get(shuffledUsersOffset));
            lastTweetID = user.getLastTweetID();
            accStatus = user.accountStatus;
            prvStatus = user.privacyStatus;
            if(lastTweetID==0 || accStatus==User.AccountStatus.INACTIVE || prvStatus==User.PrivacyStatus.PRIVATE ||
                    Main.mainUser.blackList.contains(user.getUserID()) || Main.mainUser.silentList.contains(user.getUserID()))
                continue;
            shownTweetOffset= 1 + (long) (Math.random() * (lastTweetID - 1));
            shownTweet = Tweet.loadTweet(shuffledUsers.get(shuffledUsersOffset),shownTweetOffset);
            Tweet.printTweet(shownTweet);
            break;
        }
    }

    public static FsjPageManager.CompleteState retweet(){
        Tweet tweet = new Tweet(shownTweet.getMsgText(), LocalDateTime.now(), Main.mainUser.getUserID(),Tweet.generateTweetID(Main.mainUser));
        try {
            tweet.saveTweet();
        } catch (Exception e) {
            //e.printStackTrace();
            LogHandler.logger.error("tweet " + tweet.getTweetID() + " from user " + tweet.getUserID() + " could not be saved.");
            return FsjPageManager.CompleteState.NONE;
        }

        System.out.println("your retweet is successfully saved.");
        LogHandler.logger.info("user " + tweet.getUserID() + " retweeted " + tweet.getTweetID());
        return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState commentListManager(){
        long commentNum = Tweet.showTweet(shownTweet.getUserID(),shownTweet.getTweetID());
        if(commentNum==0){
            System.out.println("this tweet does not have any comment yet.");
            return FsjPageManager.CompleteState.NONE;
        }
        shownCommentOffset=1;
        shownComment = Comment.loadComment(shownTweet.getUserID(),shownTweet.getTweetID(),shownCommentOffset);
        Comment.printComment(shownComment);

        while(true){
            if(commentList.getCommandParser().listenToUser()){
                switch (commentList.getCommandParser().getCommand()){
                    case "move":
                        if (commentList.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--next")) {
                            ExplorerPage.moveComment(1);
                        }
                        else if(commentList.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--previous")){
                            ExplorerPage.moveComment(-1);
                        }
                        else{
                            commentList.getCommandParser().improperInput(true, "");
                        }
                        break;
                    case "like":
                        shownComment.likeList.add(Main.mainUser.getUserID());
                        break;
                    case "addto":
                        if (commentList.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--saved")) {
                            commentList.getCommandParser().improperInput(true, "");
                            break;
                        }
                        break;
                    case "forward":
                        break;
                    case "block":
                        Main.mainUser.blackList.add(shownComment.getUserID());
                        break;
                    case "silent":
                        Main.mainUser.silentList.add(shownComment.getUserID());
                        break;
                    case "report":
                        Main.report("user "+shownComment.getUserID()+" ,tweet "+shownComment.getTweetID()+" ,comment "+shownComment.getCommentID());
                        break;
                    case "goto":
                        if (commentList.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--user")) {
                            commentList.getCommandParser().improperInput(true, "");
                            break;

                        }
                        return PersonalPage.visitAnotherUser(shownComment.getUserID());
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    default:
                        break;
                }
            }
        }
    }

    public static void moveComment(int step){
        long commentNum = Tweet.loadTweet(shownTweet.getUserID(),shownTweet.getTweetID()).getLastCommentID();

        if(step==1){
            if(shownCommentOffset==commentNum)
                shownCommentOffset=0;
            shownCommentOffset++;
        }else if(step==-1){
            if(shownCommentOffset==1)
                shownCommentOffset=commentNum;
            shownCommentOffset--;
        }else
            return;
        shownComment = Comment.loadComment(shownTweet.getUserID(),shownTweet.getTweetID(),shownCommentOffset);
        Comment.printComment(shownComment);
    }
}
