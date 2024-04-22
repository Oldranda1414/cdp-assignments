package macropart1.simtrafficbase.states.state;

import macropart1.simengine.AgentState;
import macropart1.simtrafficbase.environment.CarDecision;
import macropart1.simtrafficbase.environment.RoadsEnv;

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
