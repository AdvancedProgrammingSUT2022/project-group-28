
import controllers.RegisterMenuController;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import views.enums.Message;

public class RegisterMenuTests {


    @BeforeAll
    public static void setUp(){
        User.getAllUsers().clear();
    }

    @Test
    public void checkAddUser(){
        //add user with username , nickname and password
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Assertions.assertEquals(1 , User.getAllUsers().size());
    }
    @Test
    public void checkUserRegisterDataWithSameUsername(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Message message = RegisterMenuController.checkUserRegisterData("ali","110","mahdi110");
        Assertions.assertEquals(Message.USERNAME_EXISTS , message);
    }


}
