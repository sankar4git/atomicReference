//package atomicMovieSeats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Solution {

	static List<AtomicReference<Integer>> seats;// Movie seats numbered as per
												// list index

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		seats = new ArrayList<>();
		for (int i = 0; i < 20; i++) {// 20 seats
			seats.add(new AtomicReference<Integer>());
		}
		Thread[] ths = new Thread[22];// 22 users
		for (int i = 0; i < ths.length; i++) {
			ths[i] = new MyTh(seats, i);
			ths[i].start();
		}
		for (Thread t : ths) {
			t.join();
		}
		for (AtomicReference<Integer> seat : seats) {
			System.out.print(" " + seat.get());
		}
	}

	/**
	 * id is the id of the user
	 * 
	 * @author sankbane
	 *
	 */
	static class MyTh extends Thread {// each thread is a user
		static AtomicInteger full = new AtomicInteger(0);
		List<AtomicReference<Integer>> l;//seats
		int id;//id of the users
		int seats;

		public MyTh(List<AtomicReference<Integer>> list, int userId) {
			l = list;
			this.id = userId;
			seats = list.size();
		}

		@Override
		public void run() {
			boolean reserved = false;
			try {
				while (!reserved && full.get() < seats) {
					Thread.sleep(50);
					int r = ThreadLocalRandom.current().nextInt(0, seats);// excludes
																			// seats
																			//
					AtomicReference<Integer> el = l.get(r);
					reserved = el.compareAndSet(null, id);// null means no user
															// has reserved this
															// seat
					if (reserved)
						full.getAndIncrement();
				}
				if (!reserved && full.get() == seats)
					System.out.println("user " + id + " did not get a seat");
			} catch (InterruptedException ie) {
				// log it
			}
		}
	}

}
