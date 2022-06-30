import ClientOperations.*;
import Server.Server;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static boolean loggedIn = false;
    public static Server workingServer = null;

    public static void main(String[] args) {
        ClientHandler clientHandler = new ClientHandler();

        if (login(clientHandler)) {
            System.out.println("Login successful");
            loggedIn = true;
        } else {
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

    public static boolean createServer(ClientHandler clientHandler) {
        System.out.println("please enter a name for your server: ");
        Scanner scanner = new Scanner(System.in);
        String name;
        do {
            name = scanner.nextLine();
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid input, please try again");
            } else
                break;
        } while (true);
        Server newServer = new Server(clientHandler.returnMainClient(), clientHandler, name);
        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        Thread clientInputHandlerThread = new Thread(clientInputHandler);
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread clientOutputHandlerThread = new Thread(clientOutputHandler);

        PortableData portableData = new PortableData("new Server", newServer);
        clientOutputHandler.setPortableData(portableData);
        clientOutputHandlerThread.start();
        clientInputHandlerThread.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for server to be created
        }
        if (response.getOrder().equals("successful")) {
            System.out.println("server created");
            clientHandler.returnMainClient().addServer(newServer);
            workingServer = newServer;
            return true;
        } else {
            System.out.println("server creation failed, please try again later");
            return false;
        }
    }

    public static boolean joinServer(ClientHandler clientHandler) {
        System.out.println("please enter name of the server you're looking for:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        do {
            if (name.trim().isEmpty() || !Pattern.matches("[a-zA-Z0-9]+", name)) {
                System.out.println("invalid input, please try again");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        Server searchingServer = new Server(null, null, name);
        PortableData portableData = new PortableData("find server", searchingServer);

        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        Thread clientInputHandlerThread = new Thread(clientInputHandler);
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread clientOutputHandlerThread = new Thread(clientOutputHandler);

        clientOutputHandler.setPortableData(portableData);
        clientOutputHandlerThread.start();
        clientInputHandlerThread.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for server to be created
        }
        if(response.getOrder().equals("successful")){
            System.out.println("welcome to the" + name + " server");
            workingServer = searchingServer;
            clientHandler.returnMainClient().addServer(searchingServer);
            return true;
        }
        else{
            System.out.println("server not found, try searching later");
            workingServer = null;
            return false;
        }
    }

    public static boolean switchServer(ClientHandler clientHandler){
        System.out.println("please enter your destination server name:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Server searchingServer = null;
        do {
            if (!name.trim().isEmpty() && !Pattern.matches("[a-zA-Z0-9]+", name)
             && ((searchingServer = clientHandler.returnMainClient().getDesiredServer(name)) != null)) {
                System.out.println("invalid input, please try again");
                name = scanner.nextLine();
            } else
                break;
        } while (true);
        PortableData portableData = new PortableData("find server", searchingServer);

        ClientInputHandler clientInputHandler = new ClientInputHandler(clientHandler, clientHandler.getClientSocket());
        Thread clientInputHandlerThread = new Thread(clientInputHandler);
        ClientOutputHandler clientOutputHandler = new ClientOutputHandler(clientHandler.getClientSocket(), clientHandler.returnMainClient());
        Thread clientOutputHandlerThread = new Thread(clientOutputHandler);

        clientOutputHandler.setPortableData(portableData);
        clientOutputHandlerThread.start();
        clientInputHandlerThread.start();

        PortableData response;
        while ((response = clientInputHandler.getPortableData()) == null) {
            //wait for server to be created
        }
        if(response.getOrder().equals("successful")){
            System.out.println("welcome to the" + name + " server");
            workingServer = searchingServer;
            return true;
        }
        else{
            System.out.println("server could not switch, try searching later");
            workingServer = null;
            return false;
        }
    }

    public static void play(ClientHandler clientHandler){
        System.out.println("""
                [1] create a new server
                [2] join a server
                [3] switch to another existing server
                [4] exit
                """);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        while(choice != 4){
            switch(choice){
                case 1 -> {
                    if(createServer(clientHandler))
                        play(clientHandler);
                    else
                        return;
                }
                case 2 -> {
                    if(joinServer(clientHandler))
                        play(clientHandler);
                    else
                        return;
                }
                case 3 -> {
                    if(switchServer(clientHandler))
                        play(clientHandler);
                    else
                        return;
                }
                default -> {
                    System.out.println("invalid choice");
                    choice = scanner.nextInt();
                }
            }
        }
    }

}
