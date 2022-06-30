package Chat;

import MessageOperations.Message;

import java.net.Socket;

public class Chat implements Runnable{

    protected ChatInputHandler chatInputHandler;
    protected ChatOutputHandler chatOutputHandler;
    protected Message message;
    @Override
    public void run() {

    }

    protected void receiveMessage(Object message){
        this.message = (Message) message;

    }
}
