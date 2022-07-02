public class ChatServices implements Runnable{
    private ClientHandler clientHandler;
    private Client client;
    private PrivateChat pvChat;
    private Group gapChat;
    private Channel channelChat;


    public ChatServices(ClientHandler clientHandler, Client client){
        this.clientHandler = clientHandler;
        this.client = client;
    }

    public void setPvChat(PrivateChat pvChat){
        this.pvChat = pvChat;
    }

    public void setGapChat(Group gapChat){
        this.gapChat = gapChat;
    }

    public void setChannelChat(Channel channelChat){
        this.channelChat = channelChat;
    }



    @Override
    public void run() {

    }
}
