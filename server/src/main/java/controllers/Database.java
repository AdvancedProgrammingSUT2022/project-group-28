package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import models.Chat;
import models.FriendshipRequest;
import models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    public Connection connect() throws ClassNotFoundException {
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;

        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");

//


        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        return connection;
    }

    public void insertData(int id, String username, String nickname, String password , int score , int profilePicNumber ,
                String lastOnline , String lastWin , int online , String friends , String friendshipRequests ,String chats ) {

        String sql = "INSERT INTO person(id , username , nickname , password , score , profilePicNumber , " +
                " lastOnline ,lastWin ,  online ,friends ,friendshipRequests ,chats) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection connection =null;
        PreparedStatement pstmt = null;
        try{
            connection = this.connect();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3,nickname);
            pstmt.setString(4,password);
            pstmt.setInt(5,score);
            pstmt.setInt(6,profilePicNumber);
            pstmt.setString(7, lastOnline);
            pstmt.setString(8,lastWin);
            pstmt.setInt(9,online);
            pstmt.setString(10, friends);
            pstmt.setString(11,friendshipRequests);
            pstmt.setString(12,chats);
            pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
    public void exportData(){
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        Connection connection =null;
        ResultSet rs = null;
        PreparedStatement statement = null;
        try {
            connection = this.connect();
            String sql = "SELECT * FROM person";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(rs.getString("username") , rs.getString("nickname") , rs.getString("password"));
                user.setId(rs.getInt("id"));
                user.setScore(rs.getInt("score"));
                user.setProfilePicNumber(rs.getInt("profilePicNumber"));
                user.setLastOnline((LocalDate) xStream.fromXML(rs.getString("lastOnline")));
                user.setLastWin((LocalDate) xStream.fromXML(rs.getString("lastWin")));
                user.setOnline(rs.getInt("online") == 1);
                user.setFriends((ArrayList<User>) xStream.fromXML(rs.getString("friends")));
                user.setFriendshipRequests((ArrayList<FriendshipRequest>) xStream.fromXML(rs.getString("friendshipRequests")));
                user.setChats((ArrayList<Chat>) xStream.fromXML(rs.getString("chats")));
                users.add(user);
            }
            User.getAllUsers().clear();
            User.setAllUsers(users);
        } catch (SQLException | ClassNotFoundException e) {
            try {
                Statement statement1 = null;
                statement1 = connection.createStatement();
                statement1.setQueryTimeout(30);  // set timeout to 30 sec.
                statement1.executeUpdate("drop table if exists person");
                statement1.executeUpdate("create table IF NOT EXISTS person (id integer, username string , nickname string , password string ," +
                        " score integer , profilePicNumber integer , lastOnline string , lastWin string ,online integer ," +
                        "friends string , friendshipRequests string , chats string )");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    public void deleteAllData() {
        String deleteSql = "delete from person";
        Connection connection =null;
        PreparedStatement pstmt = null;
        try {
            connection = this.connect();
            pstmt = connection.prepareStatement(deleteSql);
            pstmt.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

