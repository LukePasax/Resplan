package daw.engine;

import java.util.concurrent.TimeUnit;

import javafx.util.Pair;

public class Conductor extends Thread {
	
	private final RPClipPlayerNotifier notifier;
	private final RPClock clock;
	private volatile boolean stopped;
	private final Pair<Long,Integer> sleepTime;

	public Conductor(RPClipPlayerNotifier notifier, RPClock clock) {
		this.notifier = notifier;
		this.clock = clock;
		this.sleepTime = this.fromDoubleMsToMsAndNs(Clock.CLOCK_STEP_UNIT);
	}
	
	@Override
	public void run() {
		this.stopped = false;
		while(!stopped) {
			notifier.update();
			System.out.println(clock.getTime());
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

	public void notifyStopped() {
		this.interrupt();
		stopped = true;		
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	private Pair<Long,Integer> fromDoubleMsToMsAndNs(Double milliseconds) {
		Long ms = milliseconds.longValue();
		Integer ns = Long.valueOf(TimeUnit.NANOSECONDS.convert(Double.valueOf(milliseconds-milliseconds.longValue()).longValue(), null)).intValue();
		return new Pair<Long,Integer>(ms,ns);
	}
}
