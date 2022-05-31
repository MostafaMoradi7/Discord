package MessageOperations;

import ClientOperations.Client;

import java.io.Serializable;

public class GroupMessage implements Serializable {
    private String messageID;
    private String name;
    private Client from;
    private Object body;

    public GroupMessage(String name, Client from, Object body) {
        this.name = name;
        this.from = from;
        this.body = body;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }


}
