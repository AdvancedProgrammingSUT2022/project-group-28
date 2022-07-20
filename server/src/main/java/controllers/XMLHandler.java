package controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.xstream.XStream;
import models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {
    public static void importDataOfUser() {
        try {
            ArrayList<User> allUsers ;
            String xml = new String(Files.readAllBytes(Paths.get("data/userInformation.xml")));
            if(xml.length() != 0){
                XStream xStream = new XStream();
                allUsers = (ArrayList<User>) xStream.fromXML(xml);
                User.setAllUsers(allUsers);
            }
        } catch (IOException e) {

        }
    }
    public static void exportDataOfUser(ArrayList<User> users){
        try {
            FileWriter fileWriter;
            if(Files.exists(Paths.get("data/userInformation.xml")))
                fileWriter = new FileWriter("data/userInformation.xml",false);
            else{
                new File("data").mkdir();
                fileWriter = new FileWriter("data/userInformation.xml",false);
            }
            fileWriter.write(new XStream().toXML(users));
            fileWriter.close();
        } catch (IOException e) {

        }
    }
}
