package com.sd.watchers;

import com.sd.Message;
import com.sd.Server;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;


public class DataConsistenceWatcher implements Watcher {

    private final ZooKeeper zk;
    private Server server;

    public DataConsistenceWatcher(ZooKeeper zk, Server server) {
        this.zk = zk;
        this.server = server;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeChildrenChanged && event.getPath().equals("/ops")) {
            try {
                List<String> children = zk.getChildren("/ops", this);
                children.sort(String::compareTo);

                String lastOne = children.get(children.size() - 1);
                byte[] data = zk.getData("/ops/" + lastOne, false, null);

                String[] op = new String(data).split(" ");
                switch (op[0]) {
                    case "write":
                        server.write(new Message(op[1], op[2]));
                        break;
                    case "delete":
                        String response = server.delete(op[1]);
                        break;
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

