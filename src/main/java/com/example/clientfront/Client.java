package com.example.clientfront;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;


public class Client implements Serializable {
    private static final long serialVersionUID = 2L;
    /*
            FIELDS
                                */

    private int clientID;
    private String username;
    private String password;
    private String email;

    private String phone_Number;
    private Status status;
    // TODO: HANDLE A PHOTO
    private String created_At;

    private HashSet<Server> servers;

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
        created_At = LocalDateTime.now().toString();
    }

    public String getUsername() {
        return username;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    public void removeServer(Server server) {
        servers.remove(server);
    }

    public boolean anyServerExists(){
        return !servers.isEmpty();
    }

    public int getClientID() {
        return clientID;
    }

    public Server getDesiredServer(String serverName){
        for (Server server : servers) {
            if (server.getServerName().equals(serverName)) {
                return server;
            }
        }
        return null;
    }
}
