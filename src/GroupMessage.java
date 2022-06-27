public class GroupMessage {
    private int messageID;
    private Client from;
    private Object body;

    public GroupMessage(int messageID, Client from, Object body) {
        this.messageID = messageID;
        this.from = from;
        this.body = body;
    }

    public int getMessageID() {
        return messageID;
    }

    public Client getFrom() {
        return from;
    }

    public Object getBody() {
        return body;
    }
}
