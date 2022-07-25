package controllers;

import com.thoughtworks.xstream.XStream;
import models.*;
import views.enums.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class SocketHandler extends Thread {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                ClientRequest clientRequest = ClientRequest.fromJson(this.dataInputStream.readUTF());
                ServerResponse serverResponse = handleRequest(clientRequest);
                this.dataOutputStream.writeUTF(serverResponse.toJson());
                this.dataOutputStream.flush();

                if (clientRequest.getRequest() == ClientRequest.Request.START_LISTEN) break;
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ServerResponse handleRequest(ClientRequest clientRequest) {
        ArrayList<String> data = clientRequest.getData();
        switch (clientRequest.getRequest()) {
            case REGISTER:
                return handleRegister(clientRequest);
            case LOGIN:
                return handleLogin(clientRequest);
            case START_LISTEN:
                return handleStartListen(clientRequest);
            case GET_USER_INFO:
                return handleGetUserInfo(clientRequest);
            case REQUEST_FRIENDSHIP:
                return handleFriendship(clientRequest);
            case ACCEPT_FRIENDSHIP:
                return handleAcceptFriendship(clientRequest);
            case REJECT_FRIENDSHIP:
                return handleRejectFriendship(clientRequest);
            case GET_WAITING_GAMES:
                return handleGetWaitingGames(clientRequest);
            case CREATE_GAME:
                return handleCreateGame(clientRequest);
            case CANCEL_GAME:
                return handleCancelGame(clientRequest);
            case ATTEND_GAME:
                return handleAttendGame(clientRequest);
            case ACCEPT_ATTEND_GAME:
                return handleAcceptAttendGame(clientRequest);
            case REJECT_ATTEND_GAME:
                return handleRejectAttendGame(clientRequest);
            case LEAVE_GAME:
                return handleLeaveGame(clientRequest);
            case START_GAME:
                return handleStartGame(clientRequest);
            case UPDATE_GAME:
                return handleUpdateGame(clientRequest);
            case SET_INITIAL_GAME:
                return handleSetInitialGame(clientRequest);
            case GET_ALL_USERS:
                return handleGetAllUsers(clientRequest);
            case CHANGE_AVATAR:
                return handleChangeAvatar(clientRequest);
            case CHANGE_NICKNAME:
                return handleChangeNickname(clientRequest);
            case CHANGE_PASSWORD:
                return handleChangePassword(clientRequest);
            case GET_CHAT_INFO:
                return handleGetChatInfo(clientRequest);
            case SEND_MESSAGE:
                return handleSendMessage(clientRequest);
            case EDIT_MESSAGE:
                return handleEditMessage(clientRequest);
            case REMOVE_MESSAGE:
                return handleRemoveMessage(clientRequest);
            case LOGOUT:
                return handleLogout(clientRequest);
            default:
                return null;
        }
    }

    private ServerResponse handleRegister(ClientRequest clientRequest) {
        ArrayList<String> data = clientRequest.getData();
        ArrayList<String> toSend = new ArrayList<>();

        Message message = RegisterMenuController.checkUserRegisterData(data.get(0), data.get(1), data.get(2));
        switch (message) {
            case USERNAME_EXISTS:
                return new ServerResponse(ServerResponse.Response.USERNAME_EXISTS, toSend);
            case NICKNAME_EXISTS:
                return new ServerResponse(ServerResponse.Response.NICKNAME_EXISTS, toSend);
            case SUCCESS:
                return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
            default:
                return null;
        }
    }

    private ServerResponse handleLogin(ClientRequest clientRequest) {
        ArrayList<String> data = clientRequest.getData();
        ArrayList<String> toSend = new ArrayList<>();

        Message message = RegisterMenuController.checkUserLoginData(data.get(0), data.get(1));
        if (message == Message.SUCCESS) {
            String token = UUID.randomUUID().toString();
            User user = User.getUserByUsername(data.get(0));
            user.setOnline(true);
            NetworkController.getInstance().getLoggedInUsers().put(token, user);

            toSend.add(token);
            toSend.add(user.toXML());

            return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
        } else {
            return new ServerResponse(ServerResponse.Response.LOGIN_ERROR, toSend);
        }
    }

    private ServerResponse handleStartListen(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        if (user.getUpdateSocket() != null) {
            return new ServerResponse(ServerResponse.Response.IS_LISTENING, toSend);
        }

        user.setUpdateSocket(this.socket);
        user.setUpdateInputStream(this.dataInputStream);
        user.setUpdateOutputStream(this.dataOutputStream);

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleGetUserInfo(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        toSend.add(user.toXML());
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleFriendship(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        User friend = User.getUserByNickname(clientRequest.getData().get(0));
        if (friend == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_NICKNAME, toSend);
        }

        if (user.equals(friend)) {
            return new ServerResponse(ServerResponse.Response.OWN_FRIENDSHIP, toSend);
        }

        if (user.getFriends().contains(friend)) {
            return new ServerResponse(ServerResponse.Response.ALREADY_FRIEND, toSend);
        }

        if (user.getWaitingFriendshipRequest(friend) != null) {
            return new ServerResponse(ServerResponse.Response.FRIENDSHIP_REQUEST_WAITING, toSend);
        }

        FriendshipRequest friendshipRequest = new FriendshipRequest(user, friend);
        user.getFriendshipRequests().add(friendshipRequest);
        friend.getFriendshipRequests().add(friendshipRequest);

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    public ServerResponse handleAcceptFriendship(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        User friend = User.getUserByNickname(clientRequest.getData().get(0));
        if (friend == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_NICKNAME, toSend);
        }

        FriendshipRequest friendshipRequest = user.getWaitingFriendshipRequest(friend);
        if (friendshipRequest == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_FRIENDSHIP, toSend);
        }

        friendshipRequest.accept();
        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    public ServerResponse handleRejectFriendship(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        User friend = User.getUserByNickname(clientRequest.getData().get(0));
        if (friend == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_NICKNAME, toSend);
        }

        FriendshipRequest friendshipRequest = user.getWaitingFriendshipRequest(friend);
        if (friendshipRequest == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_FRIENDSHIP, toSend);
        }

        friendshipRequest.reject();
        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }


    private ServerResponse handleLogout(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        user.setLastOnline(LocalDate.now());
        user.setOnline(false);

        NetworkController.getInstance().getLoggedInUsers().remove(clientRequest.getToken());
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleGetWaitingGames(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();
        toSend.add(WaitingGame.waitingGamesToXML(WaitingGame.getWaitingGames()));

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleCreateGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        WaitingGame waitingGame = new WaitingGame(user);
        WaitingGame.getWaitingGames().add(waitingGame);

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleCancelGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        if (!WaitingGame.isAdmin(user)) {
            return new ServerResponse(ServerResponse.Response.NOT_ADMIN, toSend);
        }

        WaitingGame.cancelGame(user);
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleAttendGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        WaitingGame waitingGame = WaitingGame.fromXML(clientRequest.getData().get(0));
        WaitingGame realWaitingGame = WaitingGame.getWaitingGameByAdminId(waitingGame.getAdmin().getId());
        if (realWaitingGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_WAITING_GAME, toSend);
        }

        if (!realWaitingGame.getAdmin().getFriends().contains(user)) {
            return new ServerResponse(ServerResponse.Response.NOT_FRIEND, toSend);
        }

        realWaitingGame.getWaitingForAccept().add(user);

        ArrayList<String> updateData = new ArrayList<>();
        updateData.add(user.toXML());
        ServerUpdate serverUpdate = new ServerUpdate(ServerUpdate.Update.ATTEND_GAME_REQUEST, updateData);

        try {
            realWaitingGame.getAdmin().getUpdateOutputStream().writeUTF(serverUpdate.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleAcceptAttendGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User admin = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (admin == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        WaitingGame waitingGame = WaitingGame.getWaitingGameByAdminId(admin.getId());
        if (waitingGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_WAITING_GAME, toSend);
        }

        User user = User.getUserByUsername(User.fromXML(clientRequest.getData().get(0)).getUsername());
        if (!waitingGame.getWaitingForAccept().contains(user)) {
            return new ServerResponse(ServerResponse.Response.INVALID_WAITING_GAME, toSend);
        }

        waitingGame.getWaitingForAccept().remove(user);
        waitingGame.getOtherPlayers().add(user);

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleRejectAttendGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User admin = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (admin == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        WaitingGame waitingGame = WaitingGame.getWaitingGameByAdminId(admin.getId());
        if (waitingGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_WAITING_GAME, toSend);
        }

        User user = User.getUserByUsername(User.fromXML(clientRequest.getData().get(0)).getUsername());
        if (!waitingGame.getWaitingForAccept().contains(user)) {
            return new ServerResponse(ServerResponse.Response.INVALID_WAITING_GAME, toSend);
        }

        waitingGame.getWaitingForAccept().remove(user);

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleLeaveGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        WaitingGame.leaveWaitingGame(user);
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleStartGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User admin = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (admin == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        WaitingGame waitingGame = WaitingGame.getWaitingGameByAdminId(admin.getId());
        if (waitingGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_WAITING_GAME, toSend);
        }

        OnlineGame onlineGame = new OnlineGame(admin, (ArrayList<User>) waitingGame.getOtherPlayers().clone());
        OnlineGame.getOnlineGames().add(onlineGame);

        toSend.add(onlineGame.toXML());

        WaitingGame.getWaitingGames().remove(waitingGame);

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleSetInitialGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User admin = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (admin == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        OnlineGame onlineGame = OnlineGame.getOnlineGameByUserID(admin.getId());
        if (onlineGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_ONLINE_GAME, toSend);
        }

        ArrayList<String> updateData = new ArrayList<>();
        String gameXML = clientRequest.getData().get(0);
        updateData.add(gameXML);

        for (User player : onlineGame.getOtherPlayers()) {
            try {
                ServerUpdate serverUpdate = new ServerUpdate(ServerUpdate.Update.SET_INITIAL_GAME, updateData);
                player.getUpdateOutputStream().writeUTF(serverUpdate.toJson());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleUpdateGame(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        OnlineGame onlineGame = OnlineGame.getOnlineGameByUserID(user.getId());
        if (onlineGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_ONLINE_GAME, toSend);
        }

        ArrayList<String> updateData = new ArrayList<>();
        String gameXML = clientRequest.getData().get(0);
        updateData.add(gameXML);

        ServerUpdate serverUpdate = new ServerUpdate(ServerUpdate.Update.UPDATE_GAME, updateData);
        String updateJson = serverUpdate.toJson();
        try {
            onlineGame.getAdmin().getUpdateOutputStream().writeUTF(updateJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (User player : onlineGame.getOtherPlayers()) {
            try {
                player.getUpdateOutputStream().writeUTF(updateJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleGetAllUsers(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        toSend.add(User.usersToXML(User.getAllUsers()));

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleChangeAvatar(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        int profilePicNumber = Integer.parseInt(clientRequest.getData().get(0));
        user.setProfilePicNumber(profilePicNumber);

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleChangeNickname(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        String nickname = clientRequest.getData().get(0);
        if (ProfileMenuController.changeNickname(user, nickname) == Message.CHANGE_NICKNAME_ERROR) {
            return new ServerResponse(ServerResponse.Response.INVALID_NICKNAME, toSend);
        }

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleChangePassword(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        Message message = ProfileMenuController.changePassword(user, clientRequest.getData().get(0),
                                                               clientRequest.getData().get(1));
        if (message == Message.INCORRECT_PASSWORD) {
            return new ServerResponse(ServerResponse.Response.INCORRECT_PASSWORD, toSend);
        } else if (message == Message.REPETITIOUS_PASSWORD) {
            return new ServerResponse(ServerResponse.Response.REPETITIOUS_PASSWORD, toSend);
        }

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleGetChatInfo(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        Chat chat = user.getChatByFriendID(Integer.parseInt(clientRequest.getData().get(0)));
        if (chat == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_CHAT, toSend);
        }

        toSend.add(chat.toXML());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleSendMessage(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        Chat chat = user.getChatByFriendID(Integer.parseInt(clientRequest.getData().get(0)));
        if (chat == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_CHAT, toSend);
        }

        ChatMessage chatMessage = new ChatMessage(user, clientRequest.getData().get(1));
        chat.addChatMessage(chatMessage);

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleEditMessage(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        Chat chat = user.getChatByFriendID(Integer.parseInt(clientRequest.getData().get(0)));
        if (chat == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_CHAT, toSend);
        }

        int messageIndex = Integer.parseInt(clientRequest.getData().get(1));
        String newMessage = clientRequest.getData().get(2);
        chat.getChatMessages().get(messageIndex).edit(newMessage);

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }

    private ServerResponse handleRemoveMessage(ClientRequest clientRequest) {
        ArrayList<String> toSend = new ArrayList<>();

        User user = NetworkController.getInstance().getLoggedInUsers().get(clientRequest.getToken());
        if (user == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_TOKEN, toSend);
        }

        Chat chat = user.getChatByFriendID(Integer.parseInt(clientRequest.getData().get(0)));
        if (chat == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_CHAT, toSend);
        }

        int messageIndex = Integer.parseInt(clientRequest.getData().get(1));
        chat.getChatMessages().remove(messageIndex);

        XMLHandler.exportDataOfUser(User.getAllUsers());

        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }
}
