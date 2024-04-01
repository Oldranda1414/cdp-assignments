package pcd.ass01.simengineconcurrent;

/**
 * This interface should be used as a state pattern for the agents, substituting the switch statement in the act method of the seq version.
*/
public interface AgentState<I> {
    
    public void act(String id, I env);
}
