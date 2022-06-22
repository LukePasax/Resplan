package daw.engine;

import resplan.Starter;
import javafx.util.Pair;

/**
 * A {@link Thread} which updates clock and the clip player notifier 
 * every {@code Clock.CLOCK_TIME_UNIT}.
 */
public final class Conductor extends Thread {
	
	private static final int MS_TO_NS = 1000000;
	
	/**
	 * The clip player notifier to update.
	 */
	private final RPClipPlayerNotifier notifier;
	
	/**
	 * The clock to update.
	 */
	private final RPClock clock;
	
	/**
	 * True if this thread is not running.
	 */
	private volatile boolean stopped;
	
	/**
	 * The sleep time of this thread, corresponding to the {@code Clock.CLOCK_TIME_UNIT}.
	 */
	private final Pair<Long, Integer> sleepTime;

	/**
	 * The time when the conductor was started.
	 */
	private long startTime;

	/**
	 * Creates a new conductor from a notifier and a clock.
	 * 
	 * @param  notifier  The clip player notifier to update.
	 * @param  clock  The clock to update.
	 */
	public Conductor(final RPClipPlayerNotifier notifier, final RPClock clock) {
		this.notifier = notifier;
		this.clock = clock;
		this.sleepTime = this.fromDoubleMsToMsAndNs(Clock.Utility.getClockStepUnit());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		this.stopped = false;
		this.startTime = System.currentTimeMillis() - clock.getTime().longValue();
		while (!stopped) {
			//notifier update
			notifier.update(clock.getStep());
			Starter.getController().updatePlaybackTime(clock.getTime());
			try {
				//sleep
				Thread.sleep(this.sleepTime.getKey(), this.sleepTime.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			//update time
			clock.setTime(Long.valueOf(System.currentTimeMillis() - startTime).doubleValue());
		}
		this.interrupt();
	}

	/**
	 * Stop this conductor.
	 */
	public void notifyStopped() {
		stopped = true;
		while (!this.isInterrupted()) {
			waitInterrupted();
		}
	}
	
	/**
	 * Check if this conductor is running.
	 * 
	 * @return {@code true} if this conductor is not running.
	 */
	public boolean isStopped() {
		return this.stopped;
	}
	
	private Pair<Long, Integer> fromDoubleMsToMsAndNs(final Double milliseconds) {
		Long ms = milliseconds.longValue();
		Integer ns = Double.valueOf((milliseconds - ms) * MS_TO_NS).intValue();
		return new Pair<Long, Integer>(ms, ns);
	}
	
	private void waitInterrupted() {
		//do nothing
	}
}
