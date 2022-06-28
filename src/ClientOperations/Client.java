package ClientOperations;

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

}
