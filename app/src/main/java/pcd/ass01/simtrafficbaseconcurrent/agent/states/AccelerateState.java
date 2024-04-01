package pcd.ass01.simtrafficbaseconcurrent.agent.states;

import pcd.ass01.simengineconcurrent.AgentState;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;

public class AccelerateState implements AgentState<RoadsEnv>{

    @Override
    public void act(String id, RoadsEnv env) {
        env.updateAgent(id, 1);
    }

}
