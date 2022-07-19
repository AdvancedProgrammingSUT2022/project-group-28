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
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
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

    public static boolean saveGame(Game game,String filename){ 
        try {
            DeflaterOutputStream deflaterOutputStream;
            if (Files.exists(Paths.get("data/save/" + filename + ".civ")))
                deflaterOutputStream = new DeflaterOutputStream (Files.newOutputStream(Paths.get("data/save/" + filename + ".civ")));
            else{
                new File("data/save").mkdir();
                deflaterOutputStream = new DeflaterOutputStream (Files.newOutputStream(Paths.get("data/save/" + filename + ".civ")));
            }
            XStream xStream = new XStream();
            deflaterOutputStream.write(xStream.toXML(game).getBytes());
            deflaterOutputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Game importGame(String filename){
        try {
            InflaterInputStream inputStream = new InflaterInputStream(Files.newInputStream(Paths.get("data/save/" + filename + ".civ")));
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String xml = scanner.hasNext() ? scanner.next() : "";
            inputStream.close();
            scanner.close();
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
