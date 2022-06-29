import java.util.ArrayList;

public class ServerDiscord {
    private Integer serverID;
    private String name;
    private ArrayList<Client> members;
    private ArrayList<Group> groups;
    private ArrayList<Channel> channels;
    private Client creator;
    private String created_At;

    public ServerDiscord(int serverID, String name, ArrayList<Client> members, ArrayList<Group> groups, ArrayList<Channel> channels, Client creator, String created_At) {
        this.serverID = serverID;
        this.name = name;
        this.members = members;
        this.groups = groups;
        this.channels = channels;
        this.creator = creator;
        this.created_At = created_At;
    }

    public Integer getServerID() {
        return serverID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Client> getMembers() {
        return members;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public Client getCreator() {
        return creator;
    }

    public String getCreated_At() {
        return created_At;
    }

    public void setServerID(Integer serverID) {
        this.serverID = serverID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(ArrayList<Client> members) {
        this.members = members;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public void setCreator(Client creator) {
        this.creator = creator;
    }

    public void setCreated_At(String created_At) {
        this.created_At = created_At;
    }

    @Override
    public String toString() {
        return "ServerDiscord{" +
                "serverID=" + serverID +
                ", name='" + name + '\'' +
                ", members=" + members +
                ", groups=" + groups +
                ", channels=" + channels +
                ", creator=" + creator +
                ", created_At='" + created_At + '\'' +
                '}';
    }
}
