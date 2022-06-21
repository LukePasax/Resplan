package daw.engine;

import daw.utilities.AudioContextManager;

/**
 * Implementation of {@link RPClock}.
 */
public final class Clock implements RPClock {
	
	/**
	 * The corrisponding time in ms to a single step of the clock.
	 */
	private static final Double CLOCK_STEP_UNIT = Double.valueOf(1 / AudioContextManager.getAudioContext().getSampleRate());
	
	/**
	 * Approximatly one year is the max time value reachable for this clock.
	 * CLOCK_MAX_TIME avoids Double rappresentation problems.
	 */
	private static final Double CLOCK_MAX_TIME = Clock.Utility.roundToExistingClockTime(3.154E10);
	
	/**
	 * The current step of the clock.
	 */
	private Long steps = 0L;
	
	/**
	 * {@inheritDoc}
	 * 
	 * throws ClockException {@inheritDoc}
	 */
	@Override
	public void step() throws ClockException {
		if (this.getTime() >= Clock.CLOCK_MAX_TIME) {
			throw new ClockException("Clock has reached the CLOCK_MAX_TIME value.");
		}
		this.steps++;
	}

	@Override
	public void reset() {
		this.steps = 0L;
	}

	@Override
	public void setTime(final Double time) {
		if (time > Clock.CLOCK_MAX_TIME) {
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

	public static final class Utility {

		/**
		 * Private constructor that ensures the non-instantiability of this utility class.
		 */
		private Utility() {
			throw new UnsupportedOperationException("Cannot instantiate an utility class.");
		}

		/**
		 * Narrow conversion from time to the corrisponding clock step.
		 * 
		 * @param  time  The time to convert in steps.
		 * 
		 * @return  The step corresponding to the given time.
		 */
		public static Long timeToClockSteps(final Double time) {
			return Double.valueOf(time / Clock.CLOCK_STEP_UNIT).longValue();
		}
	
		/**
		 * Conversion from clockStep to the corrisponding time.
		 * 
		 * @param  clockStep  The step to convert in time.
		 * 
		 * @return  The time corresponding to the given step.
		 */
		public static Double clockStepToTime(final Long clockStep) {
			return clockStep * Clock.CLOCK_STEP_UNIT;
		}
	
		/**
		 * Get the corrisponding time in ms to a single step of the clock.
		 * 
		 * @return  The clock step unit in milliseconds.
		 */
		public static Double getClockStepUnit() {
			return Clock.CLOCK_STEP_UNIT;
		}
	
		/**
		 * Get the clock max time value reachable for the clock.
		 * 
		 * @return  The clock max time value in milliseconds.
		 */
		public static Double getClockMaxTime() {
			return CLOCK_MAX_TIME;
		}

		/**
		 * Get the clock max step value reachable for the clock.
		 * 
		 * @return  The clock max step.
		 */
		public static Long getClockMaxStep() {
			return Clock.Utility.timeToClockSteps(Clock.CLOCK_MAX_TIME);
		}

		/**
		 * Round the given time to the closest existing time.
		 * 
		 * @param  time  The time to round.
		 * 
		 * @return  The existing time closest to the given time.
		 */
		public static Double roundToExistingClockTime(final Double time) {
		return Clock.Utility.clockStepToTime(Clock.Utility.timeToClockSteps(time));
		}
	}

}
