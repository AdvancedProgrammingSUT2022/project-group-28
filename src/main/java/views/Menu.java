package views;

import java.util.Scanner;

import com.sanityinc.jargs.CmdLineParser;

import models.User;

public abstract class Menu {
    protected User loggedInUser;
    protected Scanner scanner = new Scanner(System.in);
    protected CmdLineParser parser = new CmdLineParser();

    public void run(){
        while (true){
            String line = scanner.nextLine();
            if(checkCommand(line)){
                break;
            }
        }
    }

    protected abstract boolean checkCommand(String command);
}
