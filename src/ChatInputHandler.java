import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatInputHandler implements Runnable{
    private Socket clientSocket;
    private Chat chat;
    private ObjectInputStream reader;
    private static boolean isRunning = true;

    public ChatInputHandler(Chat chat, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.chat = chat;
        try {
            reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setIsRunning(boolean isRunning) {
        ChatInputHandler.isRunning = isRunning;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                PortableData receivedData = (PortableData) reader.readObject();
                if (!receivedData.getOrder().equals("unsuccessful")){
                    chat.receiveMessage(receivedData.getObject());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
