import java.time.LocalDateTime;

public class Message {
    private String messageID;
    private Client from;
    private LocalDateTime dateTime;
    private Object body;
    private TypeMVF type;

    public Message(TypeMVF type,Client from, Object body) {
        this.from = from;
        this.body = body;
        dateTime = LocalDateTime.now();
        this.type = type;
    }

    protected void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    protected Client getFrom() {
        return from;
    }

    public Object getBody() {
        return body;
    }


    protected LocalDateTime getDateTime() {
        return dateTime;
    }

}
