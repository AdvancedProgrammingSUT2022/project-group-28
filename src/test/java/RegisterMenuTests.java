
import controllers.RegisterMenuController;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RegisterMenuTests {


    @Test
    public void checkGetUserByUsernameReturnNotNull(){
        RegisterMenuController.addUser("ali" , "ali110" , "110");
        Assertions.assertNotNull(User.getUserByUsername("ali"));
    }
    

    @Test
    public void checkAddUser(){
        //add user with username , nickname and password
        int numberOfUser = User.getAllUsers().size();
        RegisterMenuController.addUser("ali" , "ali110" , "110");
        Assertions.assertEquals(numberOfUser+1 , User.getAllUsers().size());
    }
}
