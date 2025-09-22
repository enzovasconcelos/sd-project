package com.sd;

import com.sd.watchers.DataConsistenceWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;


public class ZooHandler {

    private ZooKeeper zk;
    private Server server;

    public ZooHandler(String zooAddress, Server server) throws IOException {
        this.zk = new ZooKeeper(zooAddress, 3000, null);
        this.server = server;
    }

    public void createRoot() throws InterruptedException, KeeperException {
        Stat s = zk.exists("/ops", false);
        if (s == null) {
            zk.create("/ops", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        List<String> children = zk.getChildren("/ops", new DataConsistenceWatcher(zk, server));
        children.sort(String::compareTo);
        System.out.println("Initial Stage:");
        for (String child : children) {
            byte[] data = zk.getData("/ops/" + child, false, null);
            String[] op = new String(data).split(" ");
            switch (op[0]) {
                case "write":
                    server.write(new Message(op[1], op[2]));
                    System.out.println("Write: " + op[1] + ", " + op[2]);
                    break;
                case "delete":
                    String response = server.delete(op[1]);
                    System.out.println("Delete: " + op[1] + ", "  + response);
                    break;
            }
        }
        System.out.println("Done\n");
    }

    public void zooWrite(String data) throws InterruptedException, KeeperException {
        String path = zk.create(
                "/ops/op-",
                data.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL
        );
    }

}
