import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class GroupQueries {
    public static void createTable() {
        Statement stmt = null;
        try {
            Connection conn = UserQueries.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE groups " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " name TEXT NOT NULL, " +

                    " server_id TEXT NOT NULL , " +

                    " creator TEXT NOT NULL," +

                    "pinnedMessage TEXT NOT NULL," +

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
    public static PortableData newGroup(Group group) {
        String sql = "INSERT INTO groups(creator,name,server_id,created_At) VALUES(?,?,?,?)";
        try (Connection conn =UserQueries.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, group.getCreator().getClientID());
            pstmt.setString(2, group.getName());
            pstmt.setInt(3,group.getServerID());
            pstmt.setString(4,group.getCreated_At());
            pstmt.executeUpdate();
            return new PortableData("ok", null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
