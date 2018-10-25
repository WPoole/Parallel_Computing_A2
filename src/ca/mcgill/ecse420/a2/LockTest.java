package ca.mcgill.ecse420.a2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
	static int numToIncrement = 0;
	static final int numThreads = 10;
	static final int numIncrementsPerThread = 2; 

	public static void testIncrement(Lock lock) {
		IncrementerThread[] threadArray = new IncrementerThread[numThreads];
		for(int i = 0; i < numThreads; i++) {
			threadArray[i] = new IncrementerThread(lock);
			threadArray[i].start();
		}
		
		for(int i = 0; i < numThreads; i++) { // Wait for all incrementer threads to die.
			try {
				threadArray[i].join();
			} catch (InterruptedException e) { /* TODO Auto-generated catch block. */ }
		}
		
		System.out.println("Expected Output: " + numThreads * numIncrementsPerThread); // Since each thread increments numToIncrement by 1.
		System.out.println("Obtained Output: " + numToIncrement);
	}

	public static void main(String[] args) {
		Filter filterLock = new Filter(numThreads);
		GarbageLock garbageLock = new GarbageLock();
		ReentrantLock lock = new ReentrantLock();
		testIncrement(filterLock);
	}
}
