package fsjMain;

import fsjAccount.User;
import fsjDataManager.JsonHandler;
import fsjPage.FsjPageManager;

import java.io.File;
import java.io.IOException;

public class Main {
    public static User mainUser;

    public static void main(String[] args) {
        Initialization.initClasses();
        FsjPageManager.CompleteState state;

        while (true) {
            state = FsjPageManager.loginPageManager();
            System.out.println(state);
            if(state == FsjPageManager.CompleteState.EXIT) exitCommand();
        }
    }

    public static void exitCommand(){
        try {
            User.saveUserClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
