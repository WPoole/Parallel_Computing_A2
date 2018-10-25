package ca.mcgill.ecse420.a2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Filter implements Lock {
	// TODO: TA said we don't need any form of atomic register, but show him that
	// the code does not work without the volatile keyword. Works perfectly with it, however.
	// I don't think that the TA's code actually works...
	private volatile int[] level;
	private volatile int[] victim;
	private int n;

	public Filter(int n) {
		this.level = new int[n];
		this.victim = new int[n]; // Use 1 to n-1.
		this.n = n;
		
		for(int i = 0; i < n; i++) {
			this.level[i] = 0;
		}
	}

	@Override
	public void lock() {
		int me = (int) (Thread.currentThread().getId() % n);
		for(int i = 1; i < n; i++) { // Attempt level i.
			level[me] = i;
			victim[i] = me;

			while((thereIsThreadTryingToEnterHigherLevel(me) && victim[i] == me)) {}
		}
	}

	private boolean thereIsThreadTryingToEnterHigherLevel(int threadId) {
		int myLevel = level[threadId];
		for(int k = 0; k < n; k++) {
			if(k == threadId) { // Want to skip ourselves.
				continue;
			}

			if(level[k] >= myLevel) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void unlock() {
		int me = (int) (Thread.currentThread().getId() % n);
		level[me] = 0;
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public Condition newCondition() {
		return null;
	}

	// TODO: This was another attempt at the spinning section. Delete before submitting.
	// Spin while conflicts exist.
//	boolean areConflicts = true;
//	while(areConflicts) {
//		areConflicts = false;
//		for(int k = 1; k < n; k++) {
//			if(k != me && level[k] >= i && victim[i] == me) {
//				areConflicts = true;
//				break;
//			}
//		}
//	}

}
