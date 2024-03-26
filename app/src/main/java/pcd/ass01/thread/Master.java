package pcd.ass01.thread;

public class Master extends Thread{

    private Latch latch;
    private int nWorkers;
    
    public void run(){

    }
    
    private void log(String message){
        synchronized(System.out){
            System.out.println("[master]: " + message);
        }
    }
}