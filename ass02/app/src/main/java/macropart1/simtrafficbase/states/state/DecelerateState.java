package pcd.ass01.simtrafficbaseconcurrent.states.state;

import pcd.ass01.simengineconcurrent.AgentState;
import pcd.ass01.simtrafficbaseconcurrent.environment.CarDecision;
import pcd.ass01.simtrafficbaseconcurrent.environment.RoadsEnv;

public class DecelerateState implements AgentState<RoadsEnv> {

    @Override
    public void act(String id, RoadsEnv env) {
        env.updateCar(id, CarDecision.DECELERATING);
    }

    @Override
    public String toString() {
        return "decelerating";
    }
}
