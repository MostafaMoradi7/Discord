import java.io.Serializable;
import java.rmi.ServerError;
import java.time.LocalDateTime;

public class PrivateChatMessage implements Serializable {
    private String messageID;
    private Client from;
    private Client to;
    private LocalDateTime dateTime;
    private Object message;

    public PrivateChatMessage(Client from, Client to, Object message) {

        this.from = from;
        this.to = to;
        this.message = message;
        dateTime = LocalDateTime.now();
    }

    public Client getFrom() {
        return from;
    }

    public Client getTo() {
        return to;
    }

    public Object getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
