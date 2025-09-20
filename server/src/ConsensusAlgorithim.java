import java.util.List;

public interface ConsensusAlgorithim {
    
   public List<Message> doConsensus(List<Message> quarantine);

}
