package simengine;

import utils.SafeTreeMap;

public class AbstractStates<T extends AbstractEnvironment<? extends AbstractAgent>> extends SafeTreeMap<String, AgentState<T>> {
}
