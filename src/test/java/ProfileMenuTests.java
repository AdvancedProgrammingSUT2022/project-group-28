import controllers.ProfileMenuController;
import controllers.RegisterMenuController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import views.enums.Message;

public class ProfileMenuTests {
    @Test
    public void checkChangeNickname(){
        RegisterMenuController.addUser("ali" , "110" , "ali110");
        RegisterMenuController.setLoggedInUser("ali");
        Message message = ProfileMenuController.changeNickname("ali110");
        Assertions.assertEquals(Message.CHANGE_NICKNAME_ERROR , message);
    }
}
