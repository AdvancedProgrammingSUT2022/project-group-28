import controllers.RegisterMenuController;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTests {
    @Test
    public void checkGetUserByUsernameReturnNotNull(){
        RegisterMenuController.addUser("ali" , "ali110" , "110");
        Assertions.assertNotNull(User.getUserByUsername("ali"));
    }
    @Test
    public void checkGetUserByUsernameReturnNull(){
        User.getAllUsers().clear();
        RegisterMenuController.addUser("ali" , "ali110" , "110");
        Assertions.assertNull(User.getUserByUsername("mahdi"));
    }
    @Test
    public void checkGetUserByUsernameReturnNullFromEmptyArrayListOfUser(){
        User.getAllUsers().clear();
        Assertions.assertNull(User.getUserByUsername("ali"));
    }
    
}
