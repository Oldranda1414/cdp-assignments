package simtrafficbase.states.state;

import simengine.AgentState;
import simtrafficbase.environment.CarDecision;
import simtrafficbase.environment.RoadsEnv;

public class AccelerateState implements AgentState<RoadsEnv> {

    @Override
    public void act(String id, RoadsEnv env) {
        env.updateCar(id, CarDecision.ACCELERATING);
    }

    @Override
    public String toString() {
        return "accelerating";
    }
}
