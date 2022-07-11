package com.example.clientfront;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerQueries {

    //test done
    public static void createTableServer() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE servers " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " name TEXT NOT NULL, " +

                    " creator INTEGER NOT NULL," +

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
    //test done
    public static void createTableServerMember() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE serverMembers " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " client INTEGER NOT NULL, " +

                    " server_id INTEGER NOT NULL," +

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
    public static ServerDiscord findServerWithID(int id) {
        String sql = "SELECT * FROM servers WHERE id = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rst = pstmt.executeQuery();
            return new ServerDiscord(rst.getInt("id"),rst.getString("name"),
                    UserQueries.findUserWithId(rst.getInt("creator")),rst.getString("created_At"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //test done   ((all the sever that a client join it just for show ))
        public static PortableData findServerClient(Client client){
        ArrayList<ServerDiscord> servers = new ArrayList<>();
        String sql = "SELECT * FROM serverMembers WHERE client = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                servers.add(findServerWithID(rst.getInt("server_id")));
            }
            return new PortableData("servers",servers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PortableData("something is wrong",null);
    }
    //test done
    public static PortableData allInformationServer(ServerDiscord serverDiscord){
        if (serverDiscord != null) {
            serverDiscord.setGroups(GroupQueries.findGroupForServer(serverDiscord.getServerID()));
            serverDiscord.setMembers(findClientForServer(serverDiscord.getServerID()));
        }
        return new PortableData("200",serverDiscord);
    }
    public static PortableData  insertNewServer(ServerDiscord serverDiscord) {
        String sql = "INSERT INTO servers(name,creator,created_At) VALUES(?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, serverDiscord.getName());
                pstmt.setInt(2, serverDiscord.getCreator().getClientID());
                pstmt.setString(3, serverDiscord.getCreated_At());
                pstmt.executeUpdate();
                findIdForServer(serverDiscord);
                insertCreatorForServer(serverDiscord);
            return new PortableData("200",serverDiscord);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
    public static PortableData  insertCreatorForServer(ServerDiscord serverDiscord) {
        String sql = "INSERT INTO serverMembers(client,server_id,created_At) VALUES(?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, serverDiscord.getCreator().getClientID());
                pstmt.setInt(2, serverDiscord.getServerID());
                pstmt.setString(3, serverDiscord.getCreated_At());
                pstmt.executeUpdate();
            return new PortableData("200",null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
    public static PortableData  insertNewMemberForServer(ServerDiscord serverDiscord) {
        String sql = "INSERT INTO serverMembers(client,server_id,created_At) VALUES(?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for ( Client s : serverDiscord.getMembers()) {
                pstmt.setInt(1, s.getClientID());
                pstmt.setInt(2, serverDiscord.getServerID());
                pstmt.setString(3, serverDiscord.getCreated_At());
                pstmt.executeUpdate();
            }
            return new PortableData("200",null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new PortableData("400", null);
        }
    }
    public static PortableData deleteMember(ServerDiscord serverDiscord){
        String sql = "DELETE FROM serverMembers WHERE server_id = ? AND client = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for ( Client s : serverDiscord.getMembers()) {
                pstmt.setInt(1,serverDiscord.getServerID());
                pstmt.setInt(2, s.getClientID());
                pstmt.executeUpdate();
            }
            return new PortableData("200",null);

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return new PortableData("400", null);
        }
    }
    public static ServerDiscord findIdForServer(ServerDiscord serverDiscord) {
        String sql2 = "SELECT * FROM servers WHERE name = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setString(1, serverDiscord.getName());
            ResultSet rst = pstmt.executeQuery();
            serverDiscord.setServerID(rst.getInt("id"));
            System.out.println(serverDiscord);
            return serverDiscord;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static HashSet<Client> findClientForServer(int server_id){
        HashSet<Client> clients = new HashSet<>();
        String sql = "SELECT * FROM serverMembers WHERE server_id = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, server_id);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                clients.add(UserQueries.findUserWithId(rst.getInt("client")));
            }
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
