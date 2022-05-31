import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


public class ClientHandler extends Thread{
    database database;
    Socket client;
    public ClientHandler(Socket client , database database){
        this.client=client;
        this.database= database;
    }
    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            while (true){
                PortableData portableData= ((PortableData) objectInputStream.readObject());
                if(Objects.equals(portableData.getOrder(), "registration")){
                    Client client = (Client) portableData.getObject();
                    PortableData sendResponse;
                    if(insertNewUserData(client) == 1){
                         sendResponse=new PortableData("successful",null);
                    }else {
                         sendResponse=new PortableData("unsuccessful",null);
                    }
                    outputStream.writeObject(sendResponse);
                }else if(Objects.equals(portableData.getOrder(),"login")){
                    Client client = (Client) portableData.getObject();
                    if (checkLogin(client) == 1){

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int insertNewUserData(Client client){
        String sql = "INSERT INTO User(username,password,email,phone_number,status,profile,created_at) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getUsername());
            pstmt.setString(2, client.getPassword());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone_Number());
            pstmt.setString(5, client.getStatus().toString());
            pstmt.setString(6, "not path");
            pstmt.setString(7, client.getCreated_At().toString());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public int checkLogin(Client client){
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (Connection conn = database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,client.getUsername());
            ResultSet rst = pstmt.executeQuery();
            System.out.println(rst);
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
}
