package com.example.clientfront;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GroupQueries {
    //test done
    public static void createTableGroup() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE groups " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " name TEXT NOT NULL, " +

                    " server_id INTEGER NOT NULL , " +

                    " creator INTEGER NOT NULL," +

                    "pinnedMessage TEXT," +

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
    public static void createTableGroupMember() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE groupMembers " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " client INTEGER NOT NULL, " +

                    " group_id INTEGER NOT NULL," +

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
    public static void createTableGroupAdmin() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE groupAdmins " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " client INTEGER NOT NULL, " +

                    " group_id INTEGER NOT NULL," +

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
    public static void createGroupMessageTable() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE groupMessage " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " group_id INTEGER NOT NULL, " +

                    " sender INTEGER NOT NULL , " +

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
    //test done
    public static PortableData newGroup(Group group) {
        String sql = "INSERT INTO groups(creator,name,server_id,created_At) VALUES(?,?,?,?)";
        try (Connection conn =UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, group.getCreator().getClientID());
            pstmt.setString(2, group.getName());
            pstmt.setInt(3,group.getServerID());
            pstmt.setString(4,group.getCreated_At());
            pstmt.executeUpdate();
            return new PortableData("200", findIdForGroup(group));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
    public static Group findIdForGroup(Group group) {
        String sql2 = "SELECT * FROM groups WHERE name = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setString(1, group.getName());
            ResultSet rst = pstmt.executeQuery();
            group.setGroupID(rst.getInt("id"));
            return group;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    // test done
    public static PortableData  insertNewGroupMessage(GroupMessage groupMessage) {
        String sql = "INSERT INTO groupMessage(group_id,sender,body,created_At) VALUES(?,?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, groupMessage.getGroupID());
            pstmt.setInt(2, groupMessage.getFrom().getClientID());
            pstmt.setString(3, groupMessage.getBody());
            pstmt.setString(4, groupMessage.getCreated_At());
            pstmt.executeUpdate();
            return new PortableData("200",null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
    //test done
    public static PortableData  insertNewGroupMembers(Group group) {
        String sql = "INSERT INTO groupMembers(group_id,server_id,client,created_At) VALUES(?,?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i=0 ; i<group.getClients().size() ; i++){
                pstmt.setInt(1, group.getGroupID());
                pstmt.setInt(2, group.getServerID());
                pstmt.setInt(3, group.getClients().get(i).getClientID());
                pstmt.setString(4, group.getCreated_At());
                pstmt.executeUpdate();
            }
            return new PortableData("200",null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
    public static PortableData deleteMember(Group group){
        String sql = "DELETE FROM groupMembers WHERE gruop_id = ? AND client = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i=0 ; i<group.getClients().size() ; i++) {
                pstmt.setInt(1,group.getGroupID());
                pstmt.setInt(2,group.getClients().get(i).getClientID());
                pstmt.executeUpdate();
            }
            return new PortableData("200",null);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return new PortableData("400", null);
        }
    }

    public static PortableData deleteAdmin(Group group){
        String sql = "DELETE FROM groupAdmins WHERE gruop_id = ? AND client = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i=0 ; i<group.getAdmins().size() ; i++) {
                pstmt.setInt(1,group.getGroupID());
                pstmt.setInt(2,group.getAdmins().get(i).getClientID());
                pstmt.executeUpdate();
            }
            return new PortableData("200",null);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return new PortableData("400", null);
        }
    }
    //test done
    public static PortableData  insertNewGroupAdmins(Group group) {
        String sql = "INSERT INTO groupAdmins(group_id,server_id,client,created_At) VALUES(?,?,?,?)";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i=0 ; i<group.getAdmins().size() ; i++){
                pstmt.setInt(1, group.getGroupID());
                pstmt.setInt(2, group.getServerID());
                pstmt.setInt(3, group.getClients().get(i).getClientID());
                pstmt.setString(4, group.getCreated_At());
                pstmt.executeUpdate();
            }
            return new PortableData("200",null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400", null);
    }
    public static HashSet<Group> findGroupForServer(int server_id){
        HashSet<Group> groups = new HashSet<>();
        String sql = "SELECT * FROM groups WHERE server_id = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, server_id);
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()) {
                groups.add(new Group(rst.getInt("id"),server_id,rst.getString("name"),
                        UserQueries.findUserWithId(rst.getInt("creator")),null,rst.getString("created_At")));
            }
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
