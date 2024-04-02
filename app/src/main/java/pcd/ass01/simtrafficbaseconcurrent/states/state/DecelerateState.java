package pcd.ass01.simtrafficbaseconcurrent.states.state;

import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.AgentState;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;

public class DecelerateState implements AgentState<CarAgent>{

    @Override
    public void act(String id, AbstractEnvironment<CarAgent> env) {
        ((RoadsEnv) env).updateAgent(id, -1);
    }

}
