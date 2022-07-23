package controllers;

import models.User;
import views.App;
import views.enums.Message;

public class ProfileMenuController {
    public static Message changeNickname(User user, String nickname) {
        User tempUser = User.getUserByNickname(nickname);
        if (tempUser != null) {
            return Message.CHANGE_NICKNAME_ERROR;
        }
        user.setNickname(nickname);
        return Message.SUCCESS;
    }

    public static Message changePassword(User user, String currentPassword, String newPassword) {
        if (!checkPassword(user, currentPassword)) {
            return Message.INCORRECT_PASSWORD;
        } else if (currentPassword.equals(newPassword)) {
            return Message.REPETITIOUS_PASSWORD;
        }
        user.setPassword(newPassword);
        return Message.SUCCESS;
    }

    private static boolean checkPassword(User user, String password) {
        if (user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
