package virtualthreads.utils;

public class Flag {
    private boolean value;

    public Flag(boolean initialValue){
        this.value = initialValue;
    }

    public synchronized void set(boolean value){
        this.value = value;
    }

    public synchronized boolean get(){
        return this.value;
    } 
}
