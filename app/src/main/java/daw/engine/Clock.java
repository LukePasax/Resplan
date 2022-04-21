package daw.engine;

public class Clock implements RPClock {
	
	private long milliseconds = 0;

	@Override
	public void step() {
		this.milliseconds += Engine.CLOCK_STEP_UNIT;
	}

	@Override
	public void reset() {
		this.milliseconds = 0;
	}

	@Override
	public void setTime(long time) {
		this.milliseconds = time;
	}

	@Override
	public long getTime() {
		return this.milliseconds;
	}

}
