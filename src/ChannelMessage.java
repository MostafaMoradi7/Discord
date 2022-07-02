import java.io.Serializable;
import java.util.ArrayList;

public class ChannelMessage extends Message implements Serializable {
    private String channelName;
    private Object body;
    private Client admin;
    private ArrayList<Client> members;
    private ArrayList<Client> admins;
    private ArrayList<ChannelMessage> messages;

    public ChannelMessage(String channelName, Object body, Client admin, TypeMVF type) {
        super(type, admin, body);
        this.channelName = channelName;
        members = new ArrayList<>();
        admins = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public Object getBody() {
        return body;
    }

    public Client getAdmin() {
        return admin;
    }

    public ArrayList<Client> getMembers() {
        return members;
    }

    public ArrayList<Client> getAdmins() {
        return admins;
    }

    public ArrayList<ChannelMessage> getMessages() {
        return messages;
    }
}
