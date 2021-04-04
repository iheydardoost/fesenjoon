package fsjMessaging;

import fsjAccount.User;
import fsjCLI.ConsoleColors;
import fsjDataManager.JsonHandler;
import fsjLogger.LogHandler;
import fsjMain.Main;
import fsjPage.FsjPageManager;
import fsjPage.Page;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Comment extends Tweet{
    private long commentID;

    public Comment() {
    }

    public Comment(String msgText, LocalDateTime msgDateTime, long userID, long tweetID, long commentID) {
        super(msgText, msgDateTime, userID,tweetID);
        this.commentID = commentID;
    }

    public static long generateCommentID(Tweet tweet){
        tweet.setLastCommentID(tweet.getLastCommentID()+1);
        return tweet.getLastCommentID();
    }

    public long getCommentID() {
        return commentID;
    }

    public static FsjPageManager.CompleteState newCommentCommand(Page page, long tweetID){
        Tweet tweet = Tweet.loadTweet(Main.mainUser.getUserID(),tweetID);
        if(tweet==null){
            page.getCommandParser().improperInput(false,"tweet not found.");
            return FsjPageManager.CompleteState.NONE;
        }

        String msg = "";
        System.out.print("type your comment\n(type \"--end\" in one line to end)\n(type \"--quit\" to cancel)\n:");
        while(true) {
            page.getCommandParser().listen();
            if(page.getCommandParser().inStr.equals("--quit")) return FsjPageManager.CompleteState.NONE;
            if(page.getCommandParser().inStr.equals("--end")) break;
            msg += page.getCommandParser().inStr;
            msg += "\n";
        }

        Comment comment = new Comment(msg,LocalDateTime.now(), Main.mainUser.getUserID(),tweetID,Comment.generateCommentID(tweet));
        try {
            comment.saveComment();
        } catch (Exception e) {
            //e.printStackTrace();
            LogHandler.logger.error("comment " + comment.getCommentID() + " from tweet " + comment.getTweetID() + " from user " +
                    comment.getUserID() + " could not be saved.");
            return FsjPageManager.CompleteState.NONE;
        }

        System.out.println("your comment is successfully saved.");
        LogHandler.logger.info("user " + comment.getUserID() + " commented " +
                comment.getCommentID() + " on " + comment.getTweetID());
        return FsjPageManager.CompleteState.NONE;
    }

    public static void showComment(long userID, long tweetID, long commentID){
        Comment comment = loadComment(userID,tweetID,commentID);
        printComment(comment);
    }

    public static Comment loadComment(long userID, long tweetID, long commentID){
        String path = ".\\src\\main\\ServerData\\Tweets\\" + Long.toHexString(userID) + "\\" + Long.toHexString(tweetID);
        path += "\\" + Long.toHexString(commentID) + ".json";
        File file = new File(path);
        if(file.isFile()){
            try {
                Comment comment = JsonHandler.mapper.readValue(file,Comment.class);
                LogHandler.logger.error("comment "+commentID+" from tweet "+tweetID+" from user "+userID+" loaded.");
                return comment;
            } catch (IOException e) {
                //e.printStackTrace();
                LogHandler.logger.error("comment "+commentID+" from tweet "+tweetID+" from user "+userID+" could not be read.");
            }
        }
        return null;
    }

    public static void printComment(Comment comment){
        String userName = User.findUserName(comment.getUserID());
        if(userName==null){
            LogHandler.logger.error("user "+comment.getUserID()+" userName could not found.");
            return;
        }
        System.out.print(ConsoleColors.CYAN);
        System.out.println("----------Comment----------");
        System.out.println(comment.getMsgText());
        System.out.println("Writer: " +userName);
        System.out.println("Date: " +comment.getMsgDateTime().toLocalDate().toString());
        System.out.println("Time: " +comment.getMsgDateTime().toLocalTime().toString());
        System.out.println("CommentID: " +comment.getCommentID());
        System.out.print(ConsoleColors.COLOR_RESET);
    }

    public void saveComment() throws Exception{
        String path = ".\\src\\main\\ServerData\\Tweets\\" + Long.toHexString(this.getUserID());
        path = path + "\\" + Long.toHexString(this.getTweetID());
        File file1 = new File(path);
        if(!file1.isDirectory()) file1.mkdir();
        path = path + "\\" + Long.toHexString(this.getCommentID()) + ".json";
        File file2 = new File(path);
        JsonHandler.mapper.writeValue(file2,this);
    }
}
