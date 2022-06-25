import java.io.Serializable;
import java.rmi.ServerError;
import java.time.LocalDateTime;

public class PrivateChatMessage implements Serializable {
    private int messageID;
    private int ChatId;
    private Client sender;
    private Client receiver;
    private String dateTime;
    private String message;

    public PrivateChatMessage(int messageID, int chatId, Client sender, Client receiver, String dateTime, String message) {
        this.messageID = messageID;
        ChatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.message = message;
    }

    public int getMessageID() {
        return messageID;
    }

    public Client getFrom() {
        return sender;
    }

    public Client getTo() {
        return receiver;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String  getMessage() {
        return message;
    }

    public int getChatId() {
        return ChatId;
    }

    @Override
    public String toString() {
        return "PrivateChatMessage{" +
                "messageID=" + messageID +
                ", ChatId=" + ChatId +
                ", from=" + sender +
                ", to=" + receiver +
                ", dateTime='" + dateTime + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}