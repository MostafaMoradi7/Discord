import ClientOperations.Client;
import ClientOperations.ClientHandler;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler();

        if (login(clientHandler)){
            System.out.println("Login successful");
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

}
