package daw.engine;

import Resplan.AudioContextManager;

public class Clock implements RPClock {
	
	/**
	 * The corrisponding time in ms to a single step of the clock.
	 * Must be a multiple of 0.5 to avoid Double approximation problems.
	 */
	protected final static Double CLOCK_STEP_UNIT = Double.valueOf(1/AudioContextManager.getAudioContext().getSampleRate());
	
	/**
	 * Approximatly one year is the max time value reachable for this clock.
	 * CLOCK_MAX_TIME avoids Double rappresentation problems.
	 */
	private final static Double CLOCK_MAX_TIME = Clock.Utility.roundToExistingClockTime(3.154E10);
	
	/**
	 * The current step of the clock.
	 */
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
		this.steps =  Clock.Utility.timeToClockSteps(time);
	}

	@Override
	public Long getStep() {
		return this.steps;
	}
	
	@Override
	public Double getTime() {
		return Clock.Utility.clockStepToTime(this.steps);
	}
	
	public static class Utility {
		
		private Utility() {
			throw new UnsupportedOperationException("Cannot instantiate an utility class.");
		}
		
		/**
		 * Narrow conversion from time to the corrisponding clock step.
		 */
		public static Long timeToClockSteps(Double time) {
			return Double.valueOf(time/Clock.CLOCK_STEP_UNIT).longValue();
		}
	
		/**
		 * Conversion from clockStep to the corrisponding time.
		 */
		public static Double clockStepToTime(Long clockStep) {
			return clockStep*Clock.CLOCK_STEP_UNIT;
		}
	
		public static Double getClockStepUnit() {
			return Clock.CLOCK_STEP_UNIT;
		}
	
		public static Double getClockMaxTime() {
			return CLOCK_MAX_TIME;
		}
		
		public static Long getClockMaxStep() {
			return Clock.Utility.timeToClockSteps(Clock.CLOCK_MAX_TIME);
			
		}
		
		public static Double roundToExistingClockTime(Double time) {
		return Clock.Utility.clockStepToTime(Clock.Utility.timeToClockSteps(time));
		}
		
	}

}
