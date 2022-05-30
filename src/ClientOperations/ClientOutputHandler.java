package ClientOperations;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientOutputHandler implements Runnable{
    private Socket clientSocket;
    private ObjectOutputStream writer;
    private PortableData portableData;

    public ClientOutputHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            writer = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPortableData(PortableData portableData) {
        this.portableData = portableData;
    }

    @Override
    public void run() {
        try {
            writer.writeObject(portableData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutdownOUT() {
        try {
            writer.close();
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
