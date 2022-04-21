package daw.engine;

public class Conductor extends Thread {
	
	private final RPClipPlayerNotifier notifier;
	private final RPClock clock;
	private volatile boolean stopped;

	public Conductor(RPClipPlayerNotifier notifier, RPClock clock) {
		this.notifier = notifier;
		this.clock = clock;
	}
	
	@Override
	public void run() {
		this.stopped = false;
		while(!stopped) {
			notifier.update();
			System.out.println(clock.getTime());
			try {
				Thread.sleep(Engine.CLOCK_STEP_UNIT);
			} catch(Exception e) {
				
			}
			clock.step();
		}
	}

	public void notifyStopped() {
		this.interrupt();
		stopped = true;		
	}
}
