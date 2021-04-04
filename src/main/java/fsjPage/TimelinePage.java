package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjLogger.LogHandler;
import fsjMain.Main;
import fsjMessaging.Comment;
import fsjMessaging.Message;
import fsjMessaging.Tweet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class TimelinePage{
    private static final Page timeline = new Page("Timeline",new CommandParser(),new ArrayList<>());
    private static final Page commentList = new Page("Comment List",new CommandParser(),new ArrayList<>());
    private static Tweet shownTweet = null;
    private static Comment shownComment = null;
    private static ArrayList<Long> shuffledFollowings = null;
    private static long shownTweetOffset = 1;
    private static int followingsOffset = 0;
    private static long shownCommentOffset=1;

    public static void initTimeline(){
        timeline.addChoice("move --next").addChoice("move --previous").addChoice("like").addChoice("addto --saved");
        timeline.addChoice("retweet").addChoice("forward").addChoice("block").addChoice("silent").addChoice("report");
        timeline.addChoice("goto --user").addChoice("goto --comment").addChoice("new --comment");
        timeline.getCommandParser().addValidCommand("exit").addValidCommand("return").addValidCommand("move").addValidTag("--next").
                addValidTag("--previous").addValidCommand("like").addValidCommand("addto").addValidTag("--saved").
                addValidCommand("retweet").addValidCommand("forward").addValidCommand("block").addValidCommand("silent").
                addValidCommand("report").addValidCommand("goto").addValidTag("--user").addValidTag("--comment").addValidCommand("new").
                addValidCommand("list");
        ////////////////////////////////////////////////////////////////////////////////
        commentList.addChoice("move --next").addChoice("move --previous").addChoice("like").addChoice("addto --saved");
        commentList.addChoice("forward").addChoice("block").addChoice("silent").addChoice("report");
        commentList.addChoice("goto --user");
        commentList.getCommandParser().addValidCommand("exit").addValidCommand("return").addValidCommand("move").addValidTag("--next").
                addValidTag("--previous").addValidCommand("like").addValidCommand("addto").addValidTag("--saved").
                addValidCommand("forward").addValidCommand("block").addValidCommand("silent").addValidCommand("report").
                addValidCommand("goto").addValidTag("--user");
    }

    public static FsjPageManager.CompleteState timelinePageManager(){
        timeline.printChoiceList();
        if(TimelinePage.chooseShownTweet()==SHOWN_TWEET_STATUS.EMPTY)
            return FsjPageManager.CompleteState.NONE;

        while(true){
            if(timeline.getCommandParser().listenToUser()){
                switch (timeline.getCommandParser().getCommand()){
                    case "move":
                        if (timeline.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--next")) {
                            TimelinePage.moveTweet(1);
                        }
                        else if(timeline.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--previous")){
                            TimelinePage.moveTweet(-1);
                        }
                        else{
                            timeline.getCommandParser().improperInput(true, "");
                        }
                        break;
                    case "like":
                        shownTweet.likeList.add(Main.mainUser.getUserID());
                        break;
                    case "addto":
                        if (timeline.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--saved")) {
                            timeline.getCommandParser().improperInput(true, "");
                            break;
                        }
                        break;
                    case "retweet":
                        TimelinePage.retweet();
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
                        Main.reportMessage(shownTweet);
                        break;
                    case "goto":
                        if (timeline.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--user")) {
                            return PersonalPage.gotoAnotherPage(shownTweet.getUserID());
                        }
                        else if(timeline.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--comment")){
                            return commentListManager();
                        }
                        else{
                            timeline.getCommandParser().improperInput(true, "");
                            break;
                        }
                    case "new":
                        if (timeline.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--comment")) {
                            timeline.getCommandParser().improperInput(true, "");
                            break;
                        }
                        return Comment.newCommentCommand(timeline,shownTweet.getTweetID());
                    case "list":
                        timeline.printChoiceList();
                        break;
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

    enum SHOWN_TWEET_STATUS{EMPTY,NORMAL}

    private static SHOWN_TWEET_STATUS chooseShownTweet(){
        if(Main.mainUser.followings.isEmpty()){
            System.out.println("you did not follow anyone yet.");
            return SHOWN_TWEET_STATUS.EMPTY;
        }
        shuffledFollowings = null;
        shuffledFollowings = (ArrayList<Long>) Main.mainUser.followings.clone();
        Collections.shuffle(shuffledFollowings);
        followingsOffset=0;
        long lastTweetID = User.loadUser(shuffledFollowings.get(followingsOffset)).getLastTweetID();
        while(lastTweetID==0){
            if(followingsOffset==shuffledFollowings.size())
                followingsOffset = 0;
            followingsOffset++;
            lastTweetID = User.loadUser(shuffledFollowings.get(followingsOffset)).getLastTweetID();
        }
        shownTweetOffset= 1 + (long) (Math.random() * (lastTweetID - 1));
        shownTweet = Tweet.loadTweet(shuffledFollowings.get(followingsOffset),shownTweetOffset);
        Tweet.printTweet(shownTweet);
        return SHOWN_TWEET_STATUS.NORMAL;
    }

    private static void moveTweet(int step){
        long lastTweetID=0;
        while(true) {
            if (step==1) {
                if (followingsOffset == shuffledFollowings.size())
                    followingsOffset = 0;
                followingsOffset++;
            } else if (step==-1) {
                if (followingsOffset == 0)
                    followingsOffset = shuffledFollowings.size();
                followingsOffset--;
            } else
                return;

            lastTweetID = User.loadUser(shuffledFollowings.get(followingsOffset)).getLastTweetID();
            if(lastTweetID==0)
                continue;
            shownTweetOffset= 1 + (long) (Math.random() * (lastTweetID - 1));
            shownTweet = Tweet.loadTweet(shuffledFollowings.get(followingsOffset),shownTweetOffset);
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
                            TimelinePage.moveComment(1);
                        }
                        else if(commentList.getCommandParser().getArgTags().get(0)==CommandParser.tagsMap.get("--previous")){
                            TimelinePage.moveComment(-1);
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
                        Main.reportMessage(shownComment);
                        break;
                    case "goto":
                        if (commentList.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--user")) {
                            commentList.getCommandParser().improperInput(true, "");
                            break;

                        }
                        return PersonalPage.gotoAnotherPage(shownComment.getUserID());
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
