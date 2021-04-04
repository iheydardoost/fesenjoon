package fsjPage;

import fsjAccount.User;
import fsjCLI.CommandParser;
import fsjMain.Main;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

public class SettingPage{
    private static final Page mainSettingPage = new Page("Setting Page",new CommandParser(),new ArrayList<>());

    public static void initSettingPage(){
        mainSettingPage.addChoice("edit --choice <XXX>\n\tprivacy\n\tlastSeen\n\taccountStatus\n\tpassword");
        mainSettingPage.addChoice("delete").addChoice("logout");
        mainSettingPage.getCommandParser().addValidCommand("edit").addValidCommand("delete").addValidCommand("logout");
        mainSettingPage.getCommandParser().addValidCommand("exit").addValidCommand("return");
        mainSettingPage.getCommandParser().addValidTag("--choice");
    }

    public static FsjPageManager.CompleteState settingPageManager(){
        FsjPageManager.CompleteState state= FsjPageManager.CompleteState.NONE;

        while(true){
            mainSettingPage.printChoiceList();
            if(mainSettingPage.getCommandParser().listenToUser()){
                switch (mainSettingPage.getCommandParser().getCommand()){
                    case "exit":
                        return FsjPageManager.CompleteState.EXIT;
                    case "return":
                        return FsjPageManager.CompleteState.NONE;
                    case "logout":
                        state = logoutUser();
                        break;
                    case "delete":
                        state = deleteUser();
                        break;
                    case "edit":
                        state = editCommand();
                        break;
                    default:
                        mainSettingPage.getCommandParser().improperInput(true,"");
                        break;
                }
                if(state == FsjPageManager.CompleteState.EXIT || state == FsjPageManager.CompleteState.LOG_OUT)
                    return state;
            }
        }

    }

    private static FsjPageManager.CompleteState editCommand(){
        if (mainSettingPage.getCommandParser().getArgs().size() != 1 &&
                mainSettingPage.getCommandParser().getArgTags().get(0)!=CommandParser.tagsMap.get("--choice")) {
            mainSettingPage.getCommandParser().improperInput(true, "");
            return FsjPageManager.CompleteState.NONE;
        }

        switch (mainSettingPage.getCommandParser().getArgs().get(0)) {
            case "privacy":
                if(mainSettingPage.getCommandParser().askYesNoQ("Do you want to set privacy\n(Y)-PUBLIC\n(N)-PRIVATE"))
                    Main.mainUser.setPrivacyStatus(User.PrivacyStatus.PUBLIC);
                else
                    Main.mainUser.setPrivacyStatus(User.PrivacyStatus.PRIVATE);
                break;
            case "lastSeen":
                editLastSeen();
                break;
            case "accountStatus":
                if(mainSettingPage.getCommandParser().askYesNoQ("Do you want to set accountStatus\n(Y)-ACTIVE\n(N)-INACTIVE"))
                    Main.mainUser.setAccountStatus(User.AccountStatus.ACTIVE);
                else
                    Main.mainUser.setAccountStatus(User.AccountStatus.INACTIVE);
                break;
            case "password":
                changePassword();
                break;
            default:
                mainSettingPage.getCommandParser().improperInput(false,"can not edit.");
                break;
        }
        return FsjPageManager.CompleteState.NONE;
    }

    private static void changePassword(){
        String inStr;
        while(true){
            System.out.print("enter old password :");
            mainSettingPage.getCommandParser().listen();
            inStr = mainSettingPage.getCommandParser().inStr;
            if (inStr.equals("--quit")) return;
            if(!Main.mainUser.isPassCorrect(inStr)){
                mainSettingPage.getCommandParser().improperInput(false, "password is not correct.");
                continue;
            }else {
                while (true) {
                    System.out.print("enter new password :");
                    mainSettingPage.getCommandParser().listen();
                    inStr = mainSettingPage.getCommandParser().inStr;
                    if (inStr.length() == 0) {
                        mainSettingPage.getCommandParser().improperInput(false, "password can not be empty.");
                        continue;
                    }
                    if (inStr.equals("--quit")) return;
                    Main.mainUser.setPasswordLength(inStr.length());
                    Main.mainUser.setPasswordHash(inStr.hashCode());
                    return;
                }
            }
        }
    }

    private static void editLastSeen(){
        if(mainSettingPage.getCommandParser().askYesNoQ("Do you want to set lastSeen\n(Y)-RECENTLY\n(N)-DATE"))
            Main.mainUser.setLastSeenStatus(User.LastSeenStatus.LAST_SEEN_RECENTLY);
        else
            Main.mainUser.setLastSeenStatus(User.LastSeenStatus.LAST_SEEN_DATE);

        System.out.println("What should be your last seen visibility? (just type a number)\n" +
                "1-FOLLOWINGS\n2-EVERYONE\n3-NO_ONE");

        while(true) {
            mainSettingPage.getCommandParser().listen();
            switch (mainSettingPage.getCommandParser().inStr) {
                case "1":
                    Main.mainUser.setLastSeenVisibility(User.LastSeenVisibility.FOLLOWINGS);
                    return;
                case "2":
                    Main.mainUser.setLastSeenVisibility(User.LastSeenVisibility.EVERYONE);
                    return;
                case "3":
                    Main.mainUser.setLastSeenVisibility(User.LastSeenVisibility.NO_ONE);
                    return;
                default:
                    System.out.println("choose a number between 1 to 3.");
                    break;
            }
        }
    }

    private static FsjPageManager.CompleteState logoutUser(){
        if(mainSettingPage.getCommandParser().askYesNoQ("Do you want to log out?"))
            return FsjPageManager.CompleteState.LOG_OUT;
        else
            return FsjPageManager.CompleteState.NONE;
    }

    private static FsjPageManager.CompleteState deleteUser(){
        if(mainSettingPage.getCommandParser().askYesNoQ("Are you sure about deleting your account?")) {
            Main.mainUser.setAccountStatus(User.AccountStatus.INACTIVE);
            User.userNameList.remove(Main.mainUser.userName);
            User.emailList.remove(Main.mainUser.email);
            return FsjPageManager.CompleteState.EXIT;
        }
        else return FsjPageManager.CompleteState.NONE;
    }
}
