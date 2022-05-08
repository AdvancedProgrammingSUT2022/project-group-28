import controllers.ProfileMenuController;
import controllers.RegisterMenuController;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import views.enums.Message;

public class ProfileMenuTests {

    @BeforeAll
    public static void setUp(){
        User.getAllUsers().clear();
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        RegisterMenuController.setLoggedInUser("ali");
    }
    @Test
    public void checkChangeNicknameWithSameNickname(){

        Message message = ProfileMenuController.changeNickname("ali110");
        Assertions.assertEquals(Message.CHANGE_NICKNAME_ERROR , message);
    }

    @Test
    public void checkChangeNicknameSuccessfully(){

        Message message = ProfileMenuController.changeNickname("mahdi110");
        Assertions.assertEquals(Message.SUCCESS , message);
    }

    @Test
    public void checkChangePasswordWithIncorrectPassword(){

        Message message = ProfileMenuController.changePassword("115" , "120");
        Assertions.assertEquals(Message.INCORRECT_PASSWORD , message);
    }

    
}
