package com.almende.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.almende.eve.transport.SyncCallback;

public class TestAsyncCallback {

	@Test
	public void testSyncCall() {
		ScheduledExecutorService scheduler = Executors
				.newScheduledThreadPool(10);
		final SyncCallback<Integer> callback1 = new SyncCallback<Integer>();
		final SyncCallback<Integer> callback2 = new SyncCallback<Integer>();
		assertNotSame(callback1, callback2);

		scheduler.schedule(new Runnable() {

			@Override
			public void run() {
				System.err.println("Send something to callback 1");
				callback1.onSuccess(1);
			}

		}, 900, TimeUnit.MILLISECONDS);
		scheduler.schedule(new Runnable() {

			@Override
			public void run() {
				System.err.println("Send something to callback 2");
				callback2.onSuccess(1);
			}

		}, 500, TimeUnit.MILLISECONDS);
		scheduler.schedule(new Runnable() {

			@Override
			public void run() {
				System.err.println("Starting waiting for callback 2");
				try {
					Integer res = callback2.get();
					assertEquals(new Integer(1), res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, 100, TimeUnit.MILLISECONDS);
		System.err.println("Starting waiting for callback 1");
		try {
			Integer res = callback1.get();
			assertEquals(new Integer(1), res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
