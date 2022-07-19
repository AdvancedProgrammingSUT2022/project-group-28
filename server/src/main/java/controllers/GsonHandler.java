package controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GsonHandler {
    public static void importDataOfUser() {
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

}
