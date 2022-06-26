import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        UserQueries userQueries = new UserQueries();
        userQueries.main();
//        try {
//            ServerSocket serverSocket = new ServerSocket(6500);
//            System.out.println("server started ...");
//            while (true){
//                Socket client = serverSocket.accept();
//                System.out.println("client connected ...");
//                ClientHandler clientHandler = new ClientHandler(client,database);
//                clientHandler.start();
//            }
//        }catch (IOException  e){
//            e.getStackTrace();
//        }
    }
}
