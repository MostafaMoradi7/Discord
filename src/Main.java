import ClientOperations.Client;
import ClientOperations.ClientHandler;
import Server.Server;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static boolean loggedIn = false;
    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler();

        if (login(clientHandler)){
            System.out.println("Login successful");
            loggedIn = true;
        }else{
            System.out.println("Login failed");
            loggedIn = false;
        }



    }

    public static boolean login(ClientHandler clientHandler) {
        System.out.println("welcome to discord");
        System.out.println("""
                [1] login
                [2] register
                [3] exit
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        boolean loggedIn = false;
        while (!loggedIn) {
            switch (choice) {
                case 1 -> {
                    if (clientHandler.loginClient() != null)
                        loggedIn = true;
                    else
                        return false;
                }
                case 2 -> {
                    if (clientHandler.registerClient() != null)
                        loggedIn = true;
                    else
                        return false;
                }
                case 3 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("invalid choice");
                    choice = scanner.nextInt();
                }
            }
        }
        return true;
    }

    public static void logout(ClientHandler clientHandler) {
        clientHandler.shutdown();
        loggedIn = false;
        System.exit(0);
    }

    public static void createServer(ClientHandler clientHandler){
        System.out.println("please enter a name for your server: ");
        Scanner scanner = new Scanner(System.in);
        String name;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches())
                System.out.println("invalid input");
        }while (true);
        Server newServer = new Server(clientHandler.returnMainClient(), clientHandler);

    }

}
