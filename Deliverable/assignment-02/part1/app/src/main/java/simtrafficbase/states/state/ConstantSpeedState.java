package simtrafficbase.states.state;

import simengine.AgentState;
import simtrafficbase.environment.CarDecision;
import simtrafficbase.environment.RoadsEnv;

public class ConstantSpeedState implements AgentState<RoadsEnv> {

    @Override
    public void act(String id, RoadsEnv env) {
        env.updateCar(id, CarDecision.CONSTANT_SPEED);
    }

    @Override
    public String toString() {
        return "constant speed";
    }
}
