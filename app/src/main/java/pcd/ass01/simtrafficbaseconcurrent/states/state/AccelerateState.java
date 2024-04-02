package pcd.ass01.simtrafficbaseconcurrent.states.state;

import pcd.ass01.simengineconcurrent.AbstractAgent;
import pcd.ass01.simengineconcurrent.AbstractEnvironment;
import pcd.ass01.simengineconcurrent.AgentState;
import pcd.ass01.simtrafficbaseconcurrent.agent.CarAgent;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;

public class AccelerateState implements AgentState<CarAgent>{

    @Override
    public void act(String id, AbstractEnvironment<? extends AbstractAgent> env) {
        ((RoadsEnv) env).updateAgent(id, 1);
    }

}