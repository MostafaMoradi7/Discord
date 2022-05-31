import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class database {

    public void main() {
        //createNewDatabase("discordProject.sqlite");
        //CreateTableUser();
        Client client = new Client("kjlj","sfsjakd",null,null,Status.DO_NOT_DISTURB);
        checkLogin(client);
    }

    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    public void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:\\Users\\Mojtaba\\Desktop\\discord project\\database" + "\\" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection connect() {
        String url = "jdbc:sqlite:C:\\Users\\Mojtaba\\Desktop\\discord project\\database\\discordProject.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void CreateTableUser() {
        Statement stmt = null;
        try {
            Connection conn = this.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE User " +

                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                    " username TEXT NOT NULL, " +

                    " password TEXT NOT NULL , " +

                    " email TEXT NOT NULL," +

                    " phone_Number TEXT," +

                    " status TEXT NOT NULL," +

                    " profile TEXT NOT NULL," +

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
    public int checkLogin(Client client){
        Client find ;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,client.getUsername());
            ResultSet rst = pstmt.executeQuery();
//            if (user not found){
//                return 0
//            }
            if (Objects.equals(rst.getString("password"), client.getPassword())){
                Client client1 = new Client(rst.getString("username"), rst.getString("password"),
                        rst.getString("email"),rst.getString("phone_number"),
                        Status.valueOf(rst.getString("Status")));
                //client1.setCreated_At(rst.getString("created_at"));
                client1.setToken(null);
                System.out.println(client1.toString());
                return 1;
            }else{
                return 2;
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return 0;
    }
}
