package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {
    public static Database database = null;

    static {
        try {
            database = new Database();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //public static void importDataOfUser() {
    //    try {
    //        ArrayList<User> allUsers ;
    //        String xml = new String(Files.readAllBytes(Paths.get("data/userInformation.xml")));
    //        if(xml.length() != 0){
    //            XStream xStream = new XStream();
    //            xStream.addPermission(AnyTypePermission.ANY);
    //            allUsers = (ArrayList<User>) xStream.fromXML(xml);
    //            User.setAllUsers(allUsers);
    //        }
    //    } catch (IOException e) {
//
    //    }
    //}
    //public static void exportDataOfUser(ArrayList<User> users){
    //    try {
    //        FileWriter fileWriter;
    //        if(Files.exists(Paths.get("data/userInformation.xml")))
    //            fileWriter = new FileWriter("data/userInformation.xml",false);
    //        else{
    //            new File("data").mkdir();
    //            fileWriter = new FileWriter("data/userInformation.xml",false);
    //        }
    //        fileWriter.write(new XStream().toXML(users));
    //        fileWriter.close();
    //    } catch (IOException e) {
//
    //    }
    //}

    public static void importDataFromDatabase(){
        database.exportData();
    }

    public static void exportDataOfUser(ArrayList<User> users){
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        for (User user: User.getAllUsers()) {
            database.insertData(user.getId(), user.getUsername(), user.getNickname(), user.getPassword() , user.getScore(),
                    user.getProfilePicNumber(), xStream.toXML(user.getLastOnline()), xStream.toXML(user.getLastWin()),
                    (user.isOnline() == true) ? 1 : 0 , xStream.toXML(user.getFriends()) , xStream.toXML(user.getFriendshipRequests()),
                    xStream.toXML(user.getChats()));
        }
        database.exportData();
    }
}
