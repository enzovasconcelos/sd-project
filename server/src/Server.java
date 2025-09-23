import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    
    private HashMap<String, String> log;
    private List<Message> quarantine;
    private List<String> group;
    private ConsensusAlgorithim consensusAlgorithim;

    public Server() {
        log = new HashMap<String, String>();
        group = new ArrayList<String>();
        quarantine = new ArrayList<Message>();
        group = new ArrayList<String>();;
        group.add("locahost:3200");
        group.add("locahost:3201");
        group.add("locahost:3202");
        group.add("locahost:3203");
        group.add("locahost:3204");
    }
    
    public void write(Message message) {
        synchronized(this) {
            quarantine.add(message);
            doConsensus(quarantine);
        }
    }
    
    public String read(String key) {
        return log.get(key);
    }
    
    public void doConsensus(List<Message> messages) {
        synchronized(this) {
            List<Message> resultConsensus = consensusAlgorithim.doConsensus(messages);
            for(Message message : resultConsensus) {
                log.put(message.key, message.value);
            }
        }
    }
    
}
