package pcd.ass01.simengineconcurrent;

import java.util.*;
import java.util.concurrent.locks.*;

import pcd.ass01.utils.Buffer;

public class BagOfTasks implements Buffer<Task> {

	private List<Task> buffer;
	private int maxSize;
	private Lock mutex;

	public BagOfTasks() {
		buffer = new LinkedList<Task>();
		mutex = new ReentrantLock();
	}

	public void put(Task item) throws InterruptedException {
		try {
			mutex.lock();
			buffer.addLast(item);
		} finally {
			mutex.unlock();
		}
	}

	public Optional<Task> get() throws InterruptedException {
		try {
			mutex.lock();
			if (isEmpty()) {
				return Optional.empty();
			}
			Task item = buffer.removeFirst();
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