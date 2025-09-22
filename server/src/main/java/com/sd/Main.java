package com.sd;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final int poolSize = 20;
    static final int PORT = 3200;

    public static void main(String[] args) {
        System.out.println("Starting Server...");
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        try {
            Server server = new Server("localhost:2181,localhost:2182,localhost:2183");

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Running...");
            for(;;) {
                Socket conn = serverSocket.accept();
                pool.execute(new Handler(conn, server));
            }
        } catch(IOException ex) {
            System.err.println("An IOException has ocurred: " + ex.getMessage());
        } catch (InterruptedException e) {
            System.err.println("InterruptedException has ocurred: " + e.getMessage());
        } catch (KeeperException e) {
            System.err.println("KeeperException occurred: " + e.getMessage());
        }
    }

}
