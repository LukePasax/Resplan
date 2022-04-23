package daw.engine;

import Resplan.AudioContextManager;

public class Clock implements RPClock {
	
	/**
	 * The corrisponding time in ms to a single step of the clock calculated from the system audio sample rate.
	 */
	protected final static Double CLOCK_STEP_UNIT = Double.valueOf(1/AudioContextManager.getAudioContext().getSampleRate());
	
	/**
	 * Approximatly one year is the max time value reachable for this clock.
	 * CLOCK_MAX_TIME avoids Double rappresentation problems.
	 */
	private final static Double CLOCK_MAX_TIME = roundToExistingClockTime(Double.valueOf(3.154E10));
	
	private Long steps = 0l;
	
	@Override
	public void step() throws ClockException {
		if(this.getTime() >= Clock.CLOCK_MAX_TIME) {
			throw new ClockException("Clock has reached the CLOCK_MAX_TIME value.");
		}
		this.steps++;
	}

	@Override
	public void reset() {
		this.steps = 0l;
	}

	@Override
	public void setTime(Double time) {
		if(time > Clock.CLOCK_MAX_TIME) {
			throw new IllegalArgumentException("Time or corresponding steps are bigger than Clock.CLOCK_MAX_TIME");
		}
		this.steps =  this.timeToClockSteps(time);
	}

	@Override
	public Long getStep() {
		return this.steps;
	}
	
	@Override
	public Double getTime() {
		return this.clockStepToTime(this.steps);
	}

	/**
	 * Narrow conversion from time to the corrisponding clock step.
	 */
	@Override
	public Long timeToClockSteps(Double time) {
		return Double.valueOf(time/Clock.CLOCK_STEP_UNIT).longValue();
	}

	/**
	 * Conversion from clockStep to the corrisponding time.
	 */
	@Override
	public Double clockStepToTime(Long clockStep) {
		return clockStep*Clock.CLOCK_STEP_UNIT;
	}

	@Override
	public Double getClockStepUnit() {
		return Clock.CLOCK_STEP_UNIT;
	}

	@Override
	public Double getClockMaxTime() {
		return CLOCK_MAX_TIME;
	}
	
	private static Double roundToExistingClockTime(Double time) {
		return (time-(time%Clock.CLOCK_STEP_UNIT));
	}


}
