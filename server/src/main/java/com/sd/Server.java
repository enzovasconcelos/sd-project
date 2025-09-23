package com.sd;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    
    private HashMap<String, String> log;
    private List<String> group;
    private ZooHandler zoohandler;
    private String lastProcessedNode;

    public Server(String zooAddress) throws InterruptedException, IOException, KeeperException {
        log = new HashMap<>();
        group = new ArrayList<>();
        zoohandler = new ZooHandler(zooAddress, this);
        lastProcessedNode = "";

        zoohandler.createRoot();
        System.out.println(log + "\n");
    }

    public ZooHandler getZoohandler() {
        return zoohandler;
    }

    public String getLastProcessedNode() {
        return lastProcessedNode;
    }

    public void setLastProcessedNode(String lastProcessedNode) {
        this.lastProcessedNode = lastProcessedNode;
    }

    public synchronized void write(Message message) {
        log.put(message.key, message.value);
    }
    
    public synchronized String read(String key) {
        return log.get(key);
    }

    public synchronized String delete(String key) {
        return log.remove(key);
    }
    
}
