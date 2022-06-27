import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class ClientHandler extends Thread {
    Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
//        try {
//            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
//            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
//            while (true) {
//                PortableData portableData = ((PortableData) objectInputStream.readObject());
//                if (Objects.equals(portableData.getOrder(), "registration")) {
//                    Client client = (Client) portableData.getObject();
//                    PortableData sendResponse;
//                    if (insertNewUserData(client) == 1) {
//                        sendResponse = new PortableData("successful", null);
//                    } else {
//                        sendResponse = new PortableData("unsuccessful", null);
//                    }
//                    outputStream.writeObject(sendResponse);
//                } else if (Objects.equals(portableData.getOrder(), "login")) {
//                    Client client = (Client) portableData.getObject();
//                    PortableData sendResponse = checkLogin(findUser(client));
//                    outputStream.writeObject(sendResponse);
//                }else if (Objects.equals(portableData.getOrder(),"new private chat")){
//                    PrivateChat privateChat = (PrivateChat) portableData.getObject();
//                    newPrivateChat(privateChat);
//                }else if(Objects.equals(portableData.getOrder(),"list of private chat")){
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}