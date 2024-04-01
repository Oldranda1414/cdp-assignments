package pcd.ass01.simtrafficbaseconcurrent.states.state;

import pcd.ass01.simengineconcurrent.AgentStates;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;

public class AccelerateState implements AgentStates<RoadsEnv>{

    @Override
    public void act(String id, RoadsEnv env) {
        env.updateAgent(id, 1);
    }

}
