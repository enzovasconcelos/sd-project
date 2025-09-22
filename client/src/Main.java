package client.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Main {
    
    static final String HOST = "localhost";
    static final int PORT = 3200;

    public static void main(String[] args) {
        String operation = args[0] + " " + args[1];
        if(args.length > 2) {
            operation += " " + args[2];
        }
        try {
            Socket coon = new Socket(HOST, PORT);
            System.out.println("Connection stabilized!");
            PrintStream print = new PrintStream(coon.getOutputStream());
            print.println(operation);
            System.out.println("req feita");
            BufferedReader in = new BufferedReader(new InputStreamReader(coon.getInputStream()));
            System.out.printf("Value readed: %s\n", in.readLine());
            print.close();
            coon.close();
        } catch(IOException ex) {
            System.err.println("An IOException has ocorred " + ex.getMessage());
        }
    }
}
