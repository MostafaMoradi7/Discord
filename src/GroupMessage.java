public class GroupMessage {
    private Integer messageID;
    private int GroupID;
    private Client from;
    private String body;
    private String created_At;

    public GroupMessage(Integer messageID, int groupID, Client from, String body,String created_At) {
        this.messageID = messageID;
        GroupID = groupID;
        this.from = from;
        this.body = body;
        this.created_At=created_At;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public Client getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }

    public String getCreated_At() {
        return created_At;
    }
}
