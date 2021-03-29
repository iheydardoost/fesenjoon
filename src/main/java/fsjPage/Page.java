package fsjPage;

import fsjCLI.CommandParser;

public class Page {
    private String pageName;
    private CommandParser commandParser;

    public Page() {
    }

    public Page(String pageName, CommandParser commandParser) {
        this.pageName = pageName;
        this.commandParser = commandParser;
    }
}
