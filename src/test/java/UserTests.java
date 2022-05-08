import controllers.RegisterMenuController;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTests {

    @BeforeEach
    public void setUp(){
        User.getAllUsers().clear();
    }
    @Test
    public void checkGetUserByUsernameReturnNotNull(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Assertions.assertNotNull(User.getUserByUsername("ali"));
    }
    @Test
    public void checkGetUserByUsernameReturnNull(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Assertions.assertNull(User.getUserByUsername("mahdi"));
    }
    @Test
    public void checkGetUserByUsernameReturnNullFromEmptyArrayListOfUser(){
        Assertions.assertNull(User.getUserByUsername("ali"));
    }
    @Test
    public void checkGetUserByNicknameReturnNotNull(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Assertions.assertNotNull(User.getUserByNickname("ali110"));
    }
    @Test
    public void checkGetUserByNicknameReturnNull(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        Assertions.assertNull(User.getUserByNickname("mahdi110"));
    }
    @Test
    public void checkGetUserByNicknameReturnNullFromEmptyArrayListOfUser(){
        Assertions.assertNull(User.getUserByNickname("ali"));
    }
}
