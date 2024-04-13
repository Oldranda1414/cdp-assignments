package pcd.ass01.utils;

public class AtomicBoolean {
    private boolean value;

    public AtomicBoolean(boolean initialValue){
        this.value = initialValue;
    }

    public synchronized void set(boolean value){
        this.value = value;
    }

    public synchronized boolean get(){
        return this.value;
    }
}
