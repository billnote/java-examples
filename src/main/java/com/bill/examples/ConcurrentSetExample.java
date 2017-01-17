package com.bill.examples;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentSetExample {

	public static void main(String[] args) throws InterruptedException {
		final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
		final AtomicInteger atom = new AtomicInteger();
		final AtomicInteger total = new AtomicInteger();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int j = 0; j < 100; j++) {
						queue.offer(String.format("i:%s, j:%s", Thread.currentThread().getId(), j));
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					atom.addAndGet(1);
				}
			});
		}

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String s = null;
					while ((s = queue.poll()) != null) {
						System.out.println(s);
						total.addAndGet(1);
					}
					System.out.println("total count:" + total.get());
					if (total.get() >= 1000) {
						System.out.println(String.format("total count:%s. \n end!", total.get()));
						atom.addAndGet(1);
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		for (Thread t : threads) {
			t.start();
		}
		thread.start();

		while (atom.get() < 11) {
			System.out.println("waiting for end.");
			Thread.sleep(5000);
		}
	}
}
