import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class ClientHandler extends Thread {
    Socket client;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    public ClientHandler(Socket client) {
        this.client = client;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                PortableData portableData = ((PortableData) objectInputStream.readObject());
                if (Objects.equals(portableData.getOrder(), "registration")) {
                    Client client = (Client) portableData.getObject();
                    objectOutputStream.writeObject(UserQueries.insertNewUserData(client));
                } else if (Objects.equals(portableData.getOrder(), "login")) {
                    Client client = (Client) portableData.getObject();
                    PortableData sendResponse = UserQueries.checkLogin(UserQueries.findUserWithUsername(client));
                    objectOutputStream.writeObject(sendResponse);
                }else if (Objects.equals(portableData.getOrder(),"new private chat")){
                    PrivateChat privateChat = (PrivateChat) portableData.getObject();
                    PrivateChatQueries.newPrivateChat(privateChat);
                }else if(Objects.equals(portableData.getOrder(),"list of private chat")){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}