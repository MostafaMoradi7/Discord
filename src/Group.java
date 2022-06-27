import java.util.ArrayList;

public class Group {
    private int groupID;
    private String name;
    private Client creator;
    private GroupMessage pinnedMessage;
    private ArrayList<Client> clients;
    private ArrayList<GroupMessage> messages;
    private ArrayList<Client> admins;
    public Group(GroupMessage pinnedMessage ,String name, Client creator) {
        this.pinnedMessage=pinnedMessage;
        this.name = name;
        this.creator = creator;
        clients = new ArrayList<>();
        messages = new ArrayList<>();
        admins = new ArrayList<>();
    }

    public int getGroupID() {
        return groupID;
    }

    public String getName() {
        return name;
    }

    public Client getCreator() {
        return creator;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<GroupMessage> getMessages() {
        return messages;
    }

    public ArrayList<Client> getAdmins() {
        return admins;
    }
}
