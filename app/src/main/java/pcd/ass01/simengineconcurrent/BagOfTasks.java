package pcd.ass01.simengineconcurrent;

import java.util.*;
import java.util.concurrent.locks.*;

import pcd.ass01.utils.Buffer;

public class BagOfTasks implements Buffer<Runnable> {

	private List<Runnable> buffer;
	private int maxSize;
	private Lock mutex;

	public BagOfTasks() {
		buffer = new LinkedList<Runnable>();
		mutex = new ReentrantLock();
	}

	public void put(Runnable item) throws InterruptedException {
		try {
			mutex.lock();
			buffer.addLast(item);
		} finally {
			mutex.unlock();
		}
	}

	public Optional<Runnable> get() throws InterruptedException {
		try {
			mutex.lock();
			if (isEmpty()) {
				return Optional.empty();
			}
			Runnable item = buffer.removeFirst();
			return Optional.of(item);
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