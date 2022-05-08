
import controllers.RegisterMenuController;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import views.Menu;
import views.enums.Message;

public class RegisterMenuTests {


    @BeforeEach
    public void setUp(){
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
    @Test
    public void checkUserRegisterDataWithSameNickname(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Message message = RegisterMenuController.checkUserRegisterData("mahdi","110","ali110");
        Assertions.assertEquals(Message.NICKNAME_EXISTS , message);
    }

    @Test
    public void checkUserRegisterDataSuccessful(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Message message = RegisterMenuController.checkUserRegisterData("mahdi","110","mahdi110");
        Assertions.assertEquals(Message.SUCCESS , message);
    }

    @Test
    public void chechUserLoginWithNotExistUsername(){
        Message message = RegisterMenuController.checkUserLoginData("ali" , "110");
        Assertions.assertEquals(Message.LOGIN_ERROR , message);
    }

    @Test
    public void chechUserLoginIncorrectPassword(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Message message = RegisterMenuController.checkUserLoginData("ali" , "111");
        Assertions.assertEquals(Message.LOGIN_ERROR , message);
    }

    @Test
    public void checkUserLoginSuccessfully(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Message message = RegisterMenuController.checkUserLoginData("ali" , "110");
        Assertions.assertEquals(Message.SUCCESS , message);
    }

    @Test
    public void checkSetLoggedInUserAfterLoginUser(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        RegisterMenuController.setLoggedInUser("ali");
        Assertions.assertEquals("ali" , Menu.getLoggedInUser().getUsername());
    }


}
