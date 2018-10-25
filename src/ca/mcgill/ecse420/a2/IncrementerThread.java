package ca.mcgill.ecse420.a2;

import java.util.Random;
import java.util.concurrent.locks.Lock;

public class IncrementerThread extends Thread implements Runnable {
	Lock lock; // Variable for type of lock we pass in.
	Random rand; // Random number so thread can sleep for random amount of time.

	// Constructor.
	public IncrementerThread(Lock lock) {
		this.lock = lock;
		this.rand = new Random();
	}

	@Override
	public void run() {
		for(int i = 0; i < LockTest.numIncrementsPerThread; i++) {
			lock.lock(); // Lock around critical section.
			increment(); // Critical section to be performed.
			System.out.println("Current value of number being incremented: " + LockTest.numToIncrement);

			// Sleep for some small random amount of time to increase likelihood of race conditions.
			try {
				Thread.sleep(rand.nextInt(1000));
			} catch (InterruptedException e) { /* TODO Auto-generated catch block. */ }

			lock.unlock(); // Unlock once finished in critical section.
		}
	}

	// Incrementer method.
	private static void increment() {
		LockTest.numToIncrement++;
	}

}
