package Chat;

import ClientOperations.Client;

public interface MemberInteraction
{
    public void addMember(Client client);
    public void removeMember(Client client);
}
