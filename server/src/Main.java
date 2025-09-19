import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final int poolSize = 20;
    static final int PORT = 3200;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        Server server = new Server();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Running...");
            for(;;) {
                Socket conn = serverSocket.accept();
                pool.execute(new Handler(conn, server));
            }
        } catch(IOException ex) {
            System.err.println("An IOException has ocurred: " + ex.getMessage());
        }
    }

}
