import java.sql.Connection;
import java.sql.Statement;

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
}
