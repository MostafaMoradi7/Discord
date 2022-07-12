package com.example.clientfront;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PrivateChatQueries {
    //test done
    public static void createTable() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE private_chats " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " client1 INTEGER NOT NULL, " +

                    " client2 INTEGER NOT NULL, " +

                    " banned INTEGER DEFAULT 0) ";

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

                    " type TEXT NOT NULL ," +

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
        String sql = "INSERT INTO private_chats(client1,client2) VALUES(?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, privateChat.getClientONE().getClientID());
            pstmt.setInt(2, privateChat.getClientTWO().getClientID());
            pstmt.executeUpdate();
            return new PortableData("200", findIdForPrivateChat(privateChat));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static PrivateChat findIdForPrivateChat(PrivateChat privateChat) {
        String sql2 = "SELECT * FROM private_chats WHERE client1 = ? AND client2 = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, privateChat.getClientONE().getClientID());
            pstmt.setInt(2, privateChat.getClientTWO().getClientID());
            ResultSet rst = pstmt.executeQuery();
            return new PrivateChat(rst.getInt("id"),privateChat.getClientONE(),privateChat.getClientTWO(),rst.getInt("banned"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
                PrivateChat privateChat = new PrivateChat(rst.getInt("id"), client, UserQueries.findUserWithId(rst.getInt("client2")),rst.getInt("banned"));
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
                PrivateChat privateChat = new PrivateChat(rst.getInt("id"), client, UserQueries.findUserWithId(rst.getInt("client1")),rst.getInt("banned"));
                privateChats.add(privateChat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("Array list private chat", privateChats);
    }

    //test done
    public static int insertNewMessagePrivateChat(PrivateChatMessage privateChatMessage) {
        String sql = "INSERT INTO privateChatMessages(chatId,sender,receiver,body,created_At,type) VALUES(?,?,?,?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, privateChatMessage.getChatId());
            pstmt.setInt(2, privateChatMessage.getSender().getClientID());
            pstmt.setInt(3, privateChatMessage.getReceiver().getClientID());
            pstmt.setString(4, privateChatMessage.getMessage());
            pstmt.setString(5, privateChatMessage.getDateTime());
            pstmt.setString(6, privateChatMessage.getType().toString());
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
                PrivateChatMessage privateChatMessage = new PrivateChatMessage( privateChat.getChatID(),
                        UserQueries.findUserWithId(rst.getInt("sender")), UserQueries.findUserWithId(rst.getInt("receiver"))
                        , rst.getString("created_At"), rst.getString("body"),TypeMVF.valueOf(rst.getString("type")));
                messages.add(privateChatMessage);
            }
            privateChat.setMessages(messages);
          //  System.out.println(messages);
            return new PortableData("privateChatMessage", privateChat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PortableData("something is wrong", null);
    }
    public static PortableData bannedPrivateChat(PrivateChat privateChat){
            String sql = "UPDATE private_chats SET banned = ? "
                    + "WHERE id = ?;";
            try (Connection conn = UserQueries.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, privateChat.getBanned());
                pstmt.setInt(2, privateChat.getChatID());
                pstmt.executeUpdate();
                return new PortableData("200", null);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return new PortableData("400", null);
    }
}
