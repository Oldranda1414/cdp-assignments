package simtrafficbase.environment;

import utils.P2d;

public class Road {

	private double len;
	private final P2d from;
	private final P2d to;

	public Road(P2d from, P2d to) {
		this.from = from;
		this.to = to;
		this.len = P2d.len(from, to);
	}
	
	public double getLen() {
		return len;
	}
	
	public P2d getFrom() {
		return from;
	}
	
	public P2d getTo() {
		return to;
	}
}
