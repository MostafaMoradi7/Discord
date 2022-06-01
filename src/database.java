import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class database {

    public void main() {
        createNewDatabase("projectAP.sqlite");
        createTableUser();
        createTablePrivateChat();
//        Client client = new Client("messi","123",null,null,Status.DO_NOT_DISTURB);
//        checkLogin(client);
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
        String url = "jdbc:sqlite:C:\\Users\\Mojtaba\\Desktop\\discord project\\database\\projectAP.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createTableUser() {
        Statement stmt = null;
        try {
            Connection conn = this.connect();
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE users " +

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
    public void createTablePrivateChat() {
        Statement stmt = null;
        try {
            Connection conn = this.connect();
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
    public Client findUserWithUsername(Client client) {
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getUsername());
            ResultSet rst = pstmt.executeQuery();
            return new Client(rst.getString("username"),rst.getString("password"),rst.getString("email"),
                    rst.getString("phone_number"),Status.valueOf(rst.getString("status")));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public PortableData checkLogin(Client client){
        try {
            Client databaseClient = findUserWithUsername(client);
            if (databaseClient == null){
                return new PortableData("user not found",null);
            }
            if (Objects.equals(databaseClient.getPassword(), client.getPassword())) {
                databaseClient.setToken("alskdfjljasdfjl");
                return new PortableData("true" , databaseClient);
            }else{
                return new PortableData("password is incorrect",null);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public PortableData newPrivateChat(PrivateChat privateChat){
        String sql = "INSERT INTO private_chats(user1,user2) VALUES(?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, privateChat.getClientONE().getClientID());
            pstmt.setInt(2, privateChat.getClientTWO().getClientID());
            pstmt.executeUpdate();
            return new PortableData("ok",null);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Client findUserWithId(int id){
        String sql = "SELECT * FROM users WHERE id;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rst = pstmt.executeQuery();
            Client client = new Client(rst.getString("username"),rst.getString("password"),rst.getString("email"),
                    rst.getString("phone_number"),Status.valueOf(rst.getString("status")));
            client.setClientID(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public PortableData listPrivateChat(Client client){
        ArrayList<PrivateChat> privateChats=new ArrayList<>();
        String sql = "SELECT * FROM private_chats WHERE client1 = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()){
                PrivateChat privateChat = new PrivateChat(client,findUserWithId(rst.getInt("client2")));
                privateChat.setChatID(rst.getInt("id"));
                privateChats.add(privateChat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String sql2 = "SELECT * FROM private_chats WHERE client2 = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, client.getClientID());
            ResultSet rst = pstmt.executeQuery();
            while (rst.next()){
                PrivateChat privateChat = new PrivateChat(client,findUserWithId(rst.getInt("client1")));
                privateChat.setChatID(rst.getInt("id"));
                privateChats.add(privateChat);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(privateChats);
        return new PortableData("Array list private chat",privateChats);
    }
}

