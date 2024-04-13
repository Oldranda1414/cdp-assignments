package pcd.ass01.simtrafficbaseconcurrent;

/**
 *
 * V2d represents a vector in a 2d space
 * 
 */
public class V2d {

    private final double x;
    private final double y;

    public V2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public static V2d makeV2d(P2d from, P2d to) {
    	return new V2d(to.x() - from.x(), to.y() - from.y());
    }

    public V2d sum(V2d v){
        return new V2d(x+v.x,y+v.y);
    }

    public double abs(){
        return (double)Math.sqrt(x*x+y*y);
    }

    public V2d getNormalized(){
        double module=(double)Math.sqrt(x*x+y*y);
        return new V2d(x/module,y/module);
    }

    public V2d mul(double fact){
        return new V2d(x*fact,y*fact);
    }

    // @Override
    // public String toString(){
    //     return "V2d("+x+","+y+")";
    // }
}
