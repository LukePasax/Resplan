package daw.engine;

public class Clock implements RPClock {
	
	private double milliseconds = 0;

	@Override
	public void step() {
		this.milliseconds += Engine.CLOCK_STEP_UNIT;
	}

	@Override
	public void reset() {
		this.milliseconds = 0;
	}

	@Override
	public void setTime(double time) {
		this.milliseconds = time;
	}

	@Override
	public double getTime() {
		return this.milliseconds;
	}

}
