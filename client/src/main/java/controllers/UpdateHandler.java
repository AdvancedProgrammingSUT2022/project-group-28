package controllers;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;

public class UpdateHandler extends Thread {
    private DataInputStream updateInputStream;
    public UpdateHandler(DataInputStream updateInputStream) {
        this.updateInputStream = updateInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String update = this.updateInputStream.readUTF();
                System.out.println(update);
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUpdates() {}
}
