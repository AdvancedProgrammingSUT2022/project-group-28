package controllers;

import models.*;
import views.enums.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
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
            case SET_INITIAL_GAME:
                return handleSetInitialGame(clientRequest);
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


        for (String token : NetworkController.getInstance().getLoggedInUsers().keySet()) {
            if (NetworkController.getInstance().getLoggedInUsers().get(token).equals(friend)) {
                if (user.getWaitingFriendshipRequest(friend) != null) {
                    return new ServerResponse(ServerResponse.Response.FRIENDSHIP_REQUEST_WAITING, toSend);
                }

                FriendshipRequest friendshipRequest = new FriendshipRequest(user, friend);
                user.getFriendshipRequests().add(friendshipRequest);
                friend.getFriendshipRequests().add(friendshipRequest);
                // TODO: add needed
                XMLHandler.exportDataOfUser(User.getAllUsers());

                return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
            }
        }

        return new ServerResponse(ServerResponse.Response.IS_OFFLINE, toSend);
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
        System.out.println("admin was valid");

        OnlineGame onlineGame = OnlineGame.getOnlineGameByUserID(admin.getId());
        if (onlineGame == null) {
            return new ServerResponse(ServerResponse.Response.INVALID_ONLINE_GAME, toSend);
        }

        System.out.println("game was valid");

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

        System.out.println("response sent");
        return new ServerResponse(ServerResponse.Response.SUCCESS, toSend);
    }
}
