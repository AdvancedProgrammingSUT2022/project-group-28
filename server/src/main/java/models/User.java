package models;

import controllers.GsonHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import views.App;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class User {
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static int lastId;
    private int id;
    private String username;
    private String nickname;
    private String password;
    private int score;
    private int profilePicNumber;

    private LocalDate lastOnline = null;
    private LocalDate lastWin = null;

    private transient Socket updateSocket;
    private transient DataInputStream updateInputStream;
    private transient DataOutputStream updateOutputStream;

    static {
        GsonHandler.importDataOfUser();
        if (allUsers.size() == 0){
            lastId = -1;
        }
        else{
            lastId = allUsers.get(allUsers.size()-1).getId();
        }
    }

    public User(String username, String nickname, String password) {
        User.lastId++;
        this.id = User.lastId;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.score = 0;
        this.profilePicNumber = 0;
    }

    public static User getUserByUsername(String username) {
        if (allUsers.size() == 0) {
            return null;
        }
        for (User user : allUsers) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static User getUserByNickname(String nickname) {
        if (allUsers.size() == 0) {
            return null;
        }
        for (User user : allUsers) {
            if (user.nickname.equals(nickname)) {
                return user;
            }
        }
        return null;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        User.allUsers = allUsers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public LocalDate getLastOnline() { return lastOnline; }

    public LocalDate getLastWin() { return lastWin; }

    public Socket getUpdateSocket() { return updateSocket; }

    public DataInputStream getUpdateInputStream() { return updateInputStream; }

    public DataOutputStream getUpdateOutputStream() { return updateOutputStream; }

    public int getProfilePicNumber() {
        return profilePicNumber;
    }

    public void setId(int id) { this.id = id; }

    public void setLastOnline(LocalDate lastOnline) { this.lastOnline = lastOnline; }

    public void setLastWin(LocalDate lastWin) { this.lastWin = lastWin; }

    public void setUpdateSocket(Socket updateSocket) { this.updateSocket = updateSocket; }

    public void setUpdateInputStream(DataInputStream updateInputStream) { this.updateInputStream = updateInputStream; }

    public void setUpdateOutputStream(DataOutputStream updateOutputStream) { this.updateOutputStream = updateOutputStream; }

    public void setProfilePicNumber(int profilePicNumber) {
        this.profilePicNumber = profilePicNumber;
    }

    public Image getAvatar(){
        if(profilePicNumber!=-1)
            return new Image(App.class.getResource("assets/image/profilePictures/" + profilePicNumber + ".png").toString());
        else if (new File("data/profilePictures/" + username + ".png").isFile())
            return new Image("file:data/profilePictures/" + username + ".png");
        else
            return new Image(App.class.getResource("assets/image/profilePictures/0.png").toString());
    }

    public void setAvatar(Image avatar){
        removeOldAvatar();
        new File("data/profilePictures").mkdirs();
        File outputFile = new File("data/profilePictures/" + username + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(avatar, null), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeOldAvatar(){
        try{
            new File ("data/avatars/" + username + ".png").delete();
        }catch (Exception e){}
    }
}
