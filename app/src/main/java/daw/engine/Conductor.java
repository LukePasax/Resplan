package daw.engine;

import java.util.concurrent.TimeUnit;

import Resplan.Starter;
import javafx.util.Pair;

/**
 * A {@link Thread} which updates clock and the clip player notifier 
 * every {@code Clock.CLOCK_TIME_UNIT}.
 *
 */
public class Conductor extends Thread {
	
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
	 * The sleep time of this thread, corrisponding to the {@code Clock.CLOCK_TIME_UNIT}.
	 */
	private final Pair<Long,Integer> sleepTime;

	/**
	 * Creates a new conductor from a notifier and a clock.
	 * 
	 * @param  notifier  The clip player notifier to update.
	 * @param  clock  The clock to update.
	 */
	public Conductor(RPClipPlayerNotifier notifier, RPClock clock) {
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
		while(!stopped) {
			notifier.update();
			Starter.getController().updatePlaybackTime(clock.getTime());
			try {
				Thread.sleep(this.sleepTime.getKey(), this.sleepTime.getValue());
			} catch(Exception e) {
				e.printStackTrace();
			}
			try {
				clock.step();
			} catch (ClockException e) {
				this.notifyStopped();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stop this conductor.
	 */
	public void notifyStopped() {
		this.interrupt();
		stopped = true;		
	}
	
	/**
	 * Check if this conductor is running.
	 * 
	 * @return {@code true} if this conductor is not running.
	 */
	public boolean isStopped() {
		return this.stopped;
	}
	
	private Pair<Long,Integer> fromDoubleMsToMsAndNs(Double milliseconds) {
		Long ms = milliseconds.longValue();
		Integer ns = Long.valueOf(TimeUnit.NANOSECONDS.convert(Double.valueOf(milliseconds-ms).longValue(), TimeUnit.MILLISECONDS)).intValue();
		return new Pair<Long,Integer>(ms,ns);
	}
}
