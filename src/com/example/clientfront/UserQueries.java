package com.example.clientfront;

import java.sql.*;
import java.util.Objects;

public class UserQueries {

    public void main() {
    }

    public static void createNewDatabase(String fileName) {

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

    public static Connection connect() {
        String url = "jdbc:sqlite:C:\\Users\\Mojtaba\\Desktop\\discord project\\database\\projectAP.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createTableUser() {
        Statement stmt = null;
        try {
            Connection conn = connect();
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



    // test done
    public static Client findUserWithUsername(Client client) {
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getUsername());
            ResultSet rst = pstmt.executeQuery();
            return new Client(rst.getInt("id"), rst.getString("username"), rst.getString("password"), rst.getString("email"),
                    rst.getString("phone_number"), Status.valueOf(rst.getString("status")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // test done
    public static PortableData checkLogin(Client client) {
        try {
            Client client1 = findUserWithUsername(client);
            if (client1 == null) {
                return new PortableData("user not found", null);
            }
            if (Objects.equals(client1.getPassword(), client.getPassword())) {
                client1.setToken("alskdfjljasdfjl");
                return new PortableData("200", client1);
            } else {
                return new PortableData("400", null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // test done
    public static Client findUserWithId(int id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rst = pstmt.executeQuery();
            return new Client(id, rst.getString("username"), rst.getString("password"), rst.getString("email"),
                    rst.getString("phone_number"), Status.valueOf(rst.getString("status")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }



    // test done
    public static PortableData insertNewUserData(Client client) {
        String sql = "INSERT INTO users(username,password,email,phone_number,status,profile,created_at) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getUsername());
            pstmt.setString(2, client.getPassword());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone_Number());
            pstmt.setString(5, client.getStatus().toString());
            pstmt.setString(6, "not path");
            pstmt.setString(7, client.getCreated_At());
            pstmt.executeUpdate();
            return new PortableData("200",findUserWithUsername(client));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new PortableData("400",null);
    }
}
