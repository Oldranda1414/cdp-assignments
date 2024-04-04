package pcd.ass01.masterworker;

import java.util.*;
import java.util.concurrent.locks.*;

import pcd.ass01.utils.Buffer;
import pcd.ass01.utils.latch.ResettableLatch;

public class BagOfTasks implements Buffer<Task> {

	private List<Task> buffer;
	private Lock mutex;
	private Condition notEmpty;
	private ResettableLatch latch;

	public BagOfTasks(ResettableLatch latch) {
		buffer = new LinkedList<Task>();
		mutex = new ReentrantLock();
		notEmpty = mutex.newCondition();
		this.latch = latch;
	}

	@Override
	public void put(Task item) throws InterruptedException {
		try {
			mutex.lock();
			buffer.addLast(item);
			notEmpty.signal();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public Task get(String id) throws InterruptedException {
		try {
			mutex.lock();
			if (buffer.size() == 0) {
				log("[" + id + "]: bag is empty, going to sleep");
				this.latch.countDown();
				notEmpty.await();
				log("[" + id + "]: woke up");
			}
			Task item = buffer.removeFirst();
			return item;
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		try{
			mutex.lock();
			return buffer.size() == 0;
		}
		finally{
			mutex.unlock();
		}
	}

    private void log(String message){
        synchronized(System.out){
            System.out.println(message);
        }
    }
}