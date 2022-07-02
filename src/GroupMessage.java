import java.io.Serializable;

public class GroupMessage extends Message implements Serializable {
    private String messageID;
    private Client from;
    private Object body;

    public GroupMessage(TypeMVF type, Client from, Object body) {
        super(type, from, body);
        this.from = from;
        this.body = body;
    }

    public Client getFrom() {
        return from;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }


}
