package ca.mcgill.ecse420.a2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Bakery implements Lock {
	private volatile boolean[] flag;
	private volatile int[] label;
	private int n;

	public Bakery(int n) {
		this.flag = new boolean[n];
		this.label = new int[n];
		this.n = n;

		for(int i = 0; i < n; i++) {
			this.flag[i] = false;
			this.label[i] = 0;
		}
	}

	@Override
	public void lock() {
		int threadId = (int) (Thread.currentThread().getId() % n);
		flag[threadId] = true;
		label[threadId] = maxLabel(label) + 1;
		while(bakeryWaitingConditionIsMet(threadId)) {}
	}

	private boolean bakeryWaitingConditionIsMet(int threadId) {
		for(int k = 0; k < n; k++) {
			if(k == threadId) { // Want to skip ourselves.
				continue;
			}
			
			if(flag[k] && (label[k] < label[threadId] || label[k] == label[threadId] && k < threadId)) {
				return true;
			}
		}
		
		return false;
	}

	private static int maxLabel(int[] label) {
		int maxInt = -1;
		for(int i = 0; i < label.length; i++) {
			if(label[i] > maxInt) {
				maxInt = label[i];
			}
		}

		return maxInt;
	}

	@Override
	public void unlock() {
		int threadId = (int) (Thread.currentThread().getId() % n);
		flag[threadId] = false;
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
}
