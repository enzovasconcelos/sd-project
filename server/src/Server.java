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
    }
    
    public void write(Message message) {
        synchronized(this) {
            quarantine.add(message);
        }
        doConsensus(quarantine);
    }
    
    public String read(String key) {
        return log.get(key);
    }
    
    public void doConsensus(List<Message> messages) {
        List<Message> resultConsensus = consensusAlgorithim.doConsensus(messages);
        synchronized(this) {
            for(Message message : resultConsensus) {
                log.put(message.key, message.value);
            }
        }
    }
    
}
