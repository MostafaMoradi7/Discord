import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;


public class Client implements Serializable {
    /*
            FIELDS
                                */
    private String clientID;
    private String username;
    private String password;
    private String email;

    private String phone_Number;
    private Status status;
    // TODO: HANDLE A PHOTO
    private LocalDateTime created_At;

    //  CLIENT HAS A LIST OF FRIENDS:
    private HashSet<Client> friends;
    //  LIST OF BANNED USERS:
    private HashSet<Client> blocked;

    // TOKEN
    private String token;

    public Client(String username, String password, String email, String phone_Number, Status status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_Number = phone_Number;
        this.status = status;
        created_At = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreated_At() {
        return created_At;
    }

    public HashSet<Client> getFriends() {
        return friends;
    }

    public HashSet<Client> getBlocked() {
        return blocked;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Client{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone_Number='" + phone_Number + '\'' +
                ", status=" + status +
                ", created_At=" + created_At +
                ", friends=" + friends +
                ", blocked=" + blocked +
                ", token='" + token + '\'' +
                '}';
    }

    public void setCreated_At(LocalDateTime created_At) {
        this.created_At = created_At;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientID() {
        return clientID;
    }
}