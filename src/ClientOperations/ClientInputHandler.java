package ClientOperations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInputHandler implements Runnable{
    private Socket clientSocket;
    private ClientHandler clientHandler;
    private ObjectInputStream reader;
    private PortableData portableData;

    public ClientInputHandler(ClientHandler clientHandler, Socket clientSocket){
        this.clientSocket = clientSocket;
        this.clientHandler = clientHandler;
        try {
            reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while (true){
                portableData = (PortableData) reader.readObject();
                System.out.println(portableData.getObject() + "    " + portableData.getOrder());
                if (portableData.getOrder().equals("PVMessage"))
                    clientHandler.receivePVMessage(portableData);
                else if (portableData.getOrder().equals("ChannelMessage"))
                    clientHandler.receiveChannelMessage(portableData);
                else if (portableData.getOrder().equals("GapMessage"))
                    clientHandler.receiveGapMessage(portableData);
                else{
                    clientHandler.setPortableData(portableData);
                }

            }
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
