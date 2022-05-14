package controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import models.Game;
import models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GsonHandler {
    public static void importDataOfUser(){
        try {
            ArrayList<User> allUsers ;
            String json = new String(Files.readAllBytes(Paths.get("data/userInformation.json")));
            if(json.length() != 0){
                allUsers = new Gson().fromJson(json , new TypeToken<List<User>>(){}.getType());
                User.setAllUsers(allUsers);
            }
        } catch (IOException e) {

        }
    }
    public static void exportDataOfUser(ArrayList<User> users){
        try {
            FileWriter fileWriter;
            if(Files.exists(Paths.get("data/userInformation.json")))
                fileWriter = new FileWriter("data/userInformation.json",false);
            else{
                new File("data").mkdir();
                fileWriter = new FileWriter("data/userInformation.json",false);
            }
            fileWriter.write(new Gson().toJson(users));
            fileWriter.close();
        } catch (IOException e) {
            
        }
    }

    public static boolean saveGame(Game game){ 
        try {
            FileWriter fileWriter;
            if(Files.exists(Paths.get("data/gameInformation.xml")))
                fileWriter = new FileWriter("data/gameInformation.xml",false);
            else{
                new File("data").mkdir();
                fileWriter = new FileWriter("data/gameInformation.xml",false);
            }
            XStream xStream = new XStream();
            fileWriter.write(xStream.toXML(game));
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Game importGame(){
        try {
            String xml = new String(Files.readAllBytes(Paths.get("data/gameInformation.xml")));
            XStream xStream = new XStream();
            xStream.addPermission(AnyTypePermission.ANY);
            if(xml.length() != 0){
                Game game = (Game)xStream.fromXML(xml);
                return game;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
