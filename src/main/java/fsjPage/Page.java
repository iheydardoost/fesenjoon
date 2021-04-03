package fsjPage;

import fsjCLI.CommandParser;

import java.util.ArrayList;

import static fsjCLI.ConsoleColors.*;

public class Page {
    private String pageName;
    private CommandParser commandParser;
    private ArrayList<String> choiceList;

    public Page() {
    }

    public Page(String pageName, CommandParser commandParser, ArrayList<String> choiceList) {
        this.pageName = pageName;
        this.commandParser = commandParser;
        this.choiceList = choiceList;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public CommandParser getCommandParser() {
        return commandParser;
    }

    public void setCommandParser(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public Page addChoice(String choice){
        if(this.choiceList.contains(choice)) return this;
        this.choiceList.add(choice);
        return this;
    }

    public void removeChoice(String choice){
        if(!this.choiceList.contains(choice)) return;
        this.choiceList.remove(choice);
    }

    public void printChoiceList(){
        System.out.print(YELLOW_BOLD);
        System.out.println("^^^^^^^^^^^^^^^^^^^^ "+pageName+" ^^^^^^^^^^^^^^^^^^^^");

        for(int i=0;i<this.choiceList.size();i++){
            System.out.println( (i+1) + "=> " + this.choiceList.get(i));
        }
        System.out.print(COLOR_RESET);
    }
}
