package com.example.clientfront;

import javax.sound.sampled.Port;
import java.sql.*;
import java.util.ArrayList;

public class FriendQueries {
    public static void createTable() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE friends " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " sender INTEGER NOT NULL , " +

                    " receiver INTEGER NOT NULL," +

                    " isAccept INTEGER DEFAULT 0 ," +

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

    public static PortableData newRequest(RequestFriend requestFriend) {
        String sql = "INSERT INTO friends(sender,receiver,created_At) VALUES(?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestFriend.getSender().getClientID());
            pstmt.setInt(2, requestFriend.getReceiver().getClientID());
            pstmt.setString(3, requestFriend.getCreated_At());
            pstmt.executeUpdate();
            return new PortableData("200", null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }

    public static PortableData listFriends(Client client) {
        // System.out.println(client.getClientID());
        ArrayList<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM friends WHERE sender = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                if (rst.getInt("isAccept") == 1) {
                    clients.add(UserQueries.findUserWithId(rst.getInt("receiver")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String sql2 = "SELECT * FROM friends WHERE receiver = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                if (rst.getInt("isAccept") == 1) {
                    clients.add(UserQueries.findUserWithId(rst.getInt("sender")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("Array list friends", clients);
    }

    public static PortableData requestResponse(RequestFriend requestFriend) {
        String sql = "UPDATE friends SET isAccept = ? "
                + "WHERE id = ?";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, requestFriend.getAccept());
            pstmt.setInt(2, requestFriend.getId());
            pstmt.executeUpdate();
            return new PortableData("200", null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }

    public static PortableData listOfRequest(Client client) {
        ArrayList<RequestFriend> requestFriends = new ArrayList<>();
        String sql2 = "SELECT * FROM friends WHERE receiver = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                if (rst.getInt("isAccept") == 0) {
                    requestFriends.add(new RequestFriend(rst.getInt("id"), UserQueries.findUserWithId(rst.getInt("sender")),
                            UserQueries.findUserWithId(rst.getInt("receiver")), 0, rst.getString("created_At")));
                }
            }
            return new PortableData("200", requestFriends);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
}
