package pcd.ass01.simengineconcurrent;

import java.util.*;
import java.util.concurrent.locks.*;

public class BagOfTasks implements Buffer<Runnable> {

	private List<Runnable> buffer;
	private int maxSize;
	private Lock mutex;
	private Condition notEmpty, notFull;

	public BagOfTasks(int size) {
		buffer = new LinkedList<Runnable>();
		maxSize = size;
		mutex = new ReentrantLock();
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
	}

	public void put(Runnable item) throws InterruptedException {
		try {
			mutex.lock();
			if (isFull()) {
				notFull.await();
			}
			buffer.addLast(item);
			notEmpty.signal();
		} finally {
			mutex.unlock();
		}
	}

	public Runnable get() throws InterruptedException {
		try {
			mutex.lock();
			if (isEmpty()) {
				notEmpty.await();
			}
			Runnable item = buffer.removeFirst();
			notFull.signal();
			return item;
		} finally {
			mutex.unlock();
		}
	}

	public boolean isFull() {
		try{
			mutex.lock();
			return buffer.size() == maxSize;
		}
		finally{
			mutex.unlock();
		}
	}

	public boolean isEmpty() {
		try{
			mutex.lock();
			return buffer.size() == 0;
		}
		finally{
			mutex.unlock();
		}
	}
}