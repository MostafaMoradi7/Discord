package ClientOperations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInputHandler implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream reader;
    private PortableData portableData;

    public ClientInputHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            portableData = (PortableData) reader.readObject();
            System.out.println(portableData.getObject() + "    " + portableData.getOrder());
        } catch (IOException e) {
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
        return portableData;
    }
}
