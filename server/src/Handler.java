import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Handler implements Runnable {
    
    private Socket conn;
    private Server server;

    public Handler(Socket conn, Server server) {
        this.conn = conn;
        this.server = server;
    }

    public void run() {
        System.out.println("entrando na thread que trata a req");
        try {
            Scanner input = new Scanner(conn.getInputStream());
            String[] request = input.nextLine().split(" ");
            System.out.println("Request recebida: " + Arrays.toString(request));
            String response = "";
            switch (request[0]) {
                case "read":
                    response = server.read(request[1]);
                    break;
                
                case "write":
                    server.write(new Message(request[1], request[2]));
                    System.out.println("vou escrever na resposta");
                    response = "writed!";
                    break;
                    
                case "consensus":
                    // TODO: algorithim consensus
                    break;
            
                default:
                    response = "operation not found";
                    break;
            }
            PrintStream resposta = new PrintStream(conn.getOutputStream());
            resposta.println(response);
            input.close();
        } catch (IOException ex) {
            System.err.println("An IOException has ocurred: " + ex.getMessage());
        }
    }

}
