package com.sd.watchers;

import com.sd.Message;
import com.sd.Server;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.Arrays;
import java.util.List;


public class DataConsistenceWatcher implements Watcher {

    private final ZooKeeper zk;
    private Server server;

    public DataConsistenceWatcher(ZooKeeper zk, Server server) {
        this.zk = zk;
        this.server = server;
    }

    public synchronized void start(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeChildrenChanged && event.getPath().equals("/ops")) {
            try {
                List<String> children = zk.getChildren("/ops", this);
                children.sort(String::compareTo);

                for (String child : children) {
                    if (child.compareTo(server.getLastProcessedNode()) > 0) {
                        byte[] data = zk.getData("/ops/" + child, false, null);
                        String[] op = new String(data).split(" ");
                        System.out.println("op_watcher_zoo: " + Arrays.toString(op));
                        switch (op[0]) {
                            case "write":
                                server.write(new Message(op[1], op[2]));
                                break;
                            case "delete":
                                server.delete(op[1]);
                                break;
                        }
                        server.setLastProcessedNode(child);
                    }

                }
                long inicio = server.getInicio();
                long fim = System.nanoTime();
                if(inicio != 0){
                    long resultado = fim - inicio;
                    System.out.println("tempo: " + resultado);
                    server.setInicio(0L);
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        start(event);
    }
}

