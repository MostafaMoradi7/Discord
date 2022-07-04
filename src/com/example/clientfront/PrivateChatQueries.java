package com.example.clientfront;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PrivateChatQueries {
    //test done
    public static void createTable() {
        Statement stmt = null;
        try {
            Connection conn =UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE private_chats " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " client1 INTEGER NOT NULL, " +

                    " client2 INTEGER NOT NULL ) ";

            stmt.executeUpdate(sql);

            stmt.close();

            conn.close();

        } catch (Exception e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());

            System.exit(0);

        }

        System.out.println("Table Product Created Successfully!!!");

    }

    // test done
    public static void createPrivateChatMessagesTable() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE privateChatMessages " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " chatId INTEGER NOT NULL, " +

                    " sender INTEGER NOT NULL , " +

                    " receiver INTEGER NOT NULL," +

                    " body TEXT NOT NULL ," +

                    " created_At TEXT NOT NULL) ";

            stmt.executeUpdate(sql);

            stmt.close();

            conn.close();

        } catch (Exception e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());

            System.exit(0);

        }

        System.out.println("Table Product Created Successfully!!!");

    }
    // test done
    public static PortableData newPrivateChat(PrivateChat privateChat) {
        String sql = "INSERT INTO private_chats(user1,user2) VALUES(?,?)";
        try (Connection conn =UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, privateChat.getClientONE().getClientID());
            pstmt.setInt(2, privateChat.getClientTWO().getClientID());
            pstmt.executeUpdate();
            return new PortableData("ok", null);
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    // test done
    public static PortableData listPrivateChat(Client client) {
       // System.out.println(client.getClientID());
        ArrayList<PrivateChat> privateChats = new ArrayList<>();
        String sql = "SELECT * FROM private_chats WHERE client1 = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                PrivateChat privateChat = new PrivateChat(rst.getInt("id"), client, UserQueries.findUserWithId(rst.getInt("client2")));
                privateChats.add(privateChat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String sql2 = "SELECT * FROM private_chats WHERE client2 = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                PrivateChat privateChat = new PrivateChat(rst.getInt("id"), client, UserQueries.findUserWithId(rst.getInt("client1")));
                privateChats.add(privateChat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(privateChats);
        return new PortableData("Array list private chat", privateChats);
    }
    //test done
    public static int insertNewMessagePrivateChat(PrivateChatMessage privateChatMessage) {
        String sql = "INSERT INTO privateChatMessages(chatId,sender,receiver,body,created_At) VALUES(?,?,?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, privateChatMessage.getChatId());
            pstmt.setInt(2, privateChatMessage.getFrom().getClientID());
            pstmt.setInt(3, privateChatMessage.getTo().getClientID());
            pstmt.setString(4, privateChatMessage.getMessage());
            pstmt.setString(5, privateChatMessage.getDateTime().toString());
            pstmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    //test done
    public static PortableData findPrivateChatMessage(PrivateChat privateChat) {
        ArrayList<PrivateChatMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM privateChatMessages WHERE ChatId = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, privateChat.getChatID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                PrivateChatMessage privateChatMessage = new PrivateChatMessage(rst.getInt("id"),privateChat.getChatID(),
                        UserQueries.findUserWithId(rst.getInt("sender")), UserQueries.findUserWithId(rst.getInt("receiver"))
                        , rst.getString("created_At"), rst.getString("body"));
                messages.add(privateChatMessage);
            }
            return new PortableData("privateChatMessage",messages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PortableData("something is wrong",null);
    }
}
