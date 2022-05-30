import ClientOperations.ClientHandler;

public class Main {
    public static void main(String[] args) {
        ClientHandler discord = new ClientHandler();
        Thread thread = new Thread(discord);
        thread.start();


    }
}
