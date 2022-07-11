package com.example.clientfront;

import java.sql.*;
import java.util.HashMap;

public class ReactionQueries {
    public static void createTable() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE reactions " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " message_id INTEGER NOT NULL ," +

                    " like1 INTEGER DEFAULT 0 ," +

                    " dislike INTEGER DEFAULT 0 ," +

                    " laugh INTEGER DEFAULT 0 ," +

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
    public static boolean checkFirstTime(Reactions reactions){
        String sql = "SELECT * FROM reactinos WHERE message_id = ?;";
        try (Connection conn = UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reactions.getMessageId());
            ResultSet rst = pstmt.executeQuery();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static PortableData addReaction(Reactions reactions){
        if (checkFirstTime(reactions)) {
                String sql2 = "UPDATE reactions SET (like , dislike , laugh) VALUE (?,?,?)"
                        + "WHERE message_id = ?";
                try (Connection conn = UserQueries.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setInt(1, reactions.getLike());
                    pstmt.setInt(2, reactions.getDislike());
                    pstmt.setInt(3, reactions.getLaugh());
                    pstmt.executeUpdate();
                    return new PortableData("200", null);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return new PortableData("400", null);
                }
        }else {
            String sql = "INSERT INTO reactions(message_id,like1,dislike,laugh,created_At) VALUES(?,?,?,?,?)";
            try (Connection conn = UserQueries.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, reactions.getMessageId());
                pstmt.setInt(2, reactions.getLike());
                pstmt.setInt(3, reactions.getDislike());
                pstmt.setInt(4, reactions.getLaugh());
                pstmt.setString(5, reactions.getCreated_At());
                pstmt.executeUpdate();
                return new PortableData("200", null);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return new PortableData("400", null);
            }
        }
    }
    public static PortableData listReaction(Group group){
        HashMap<Integer , Reactions> messages = new HashMap<>();
        for (int i=0 ; i<group.getMessages().size() ; i++){
            String sql = "SELECT * FROM reactinos WHERE message_id = ?;";
            try (Connection conn = UserQueries.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, group.getMessages().get(i).getMessageID());
                ResultSet rst = pstmt.executeQuery();
                messages.put(group.getMessages().get(i).getMessageID(),new Reactions(rst.getInt("message_id"),rst.getInt("like1"),
                        rst.getInt("dislike"),rst.getInt("laugh")));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return new PortableData("400",null);
            }
        }
        return new PortableData("200",messages);
    }
}
