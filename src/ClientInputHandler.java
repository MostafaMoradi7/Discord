import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInputHandler implements Runnable{
    private Socket clientSocket;
    private ClientHandler clientHandler;
    private ObjectInputStream reader;
    private PortableData portableData;
    private static boolean isRunning = true;

    public ClientInputHandler(ClientHandler clientHandler, Socket clientSocket){
        this.clientSocket = clientSocket;
        this.clientHandler = clientHandler;
        try {
            reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setIsRunning(boolean isRunning) {
        ClientInputHandler.isRunning = isRunning;
    }
    @Override
    public void run() {
        try {
            System.out.println("ClientInputHandler is running");
            portableData = (PortableData) reader.readObject();
            System.out.println(portableData.getOrder() + ": " + portableData.getObject());
            clientHandler.setPortableData(portableData);


        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shutdownIN() {
        try {
            reader.close();
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PortableData getPortableData(){
        PortableData tmpData = portableData;
        portableData = null;
        return tmpData;
    }
}
