package daw.engine;

/**
 * A clock ticks the current playback time.
 * <p>The clock current time is updated in steps. 
 * Each step of the clock corresponds to a pre-definite time interval, so the clock
 * do not memorize all possible time values.
 * <br>The clock could be set to a specific time. 
 * In this case the given time will be converted to an existing clock time.
 */
public interface RPClock {
	
	/**
	 * Add one step to the current time.
	 * 
	 * @throws  ClockException  If clock max time is reached.
	 */
	void step() throws ClockException;
	
	/**
	 * Reset the current time to zero.
	 */
	void reset();
	
	/**
	 * Set the current time to the closest existing time.
	 * 
	 * @param  time  The time to set in milliseconds.
	 */
	void setTime(Double time);
	
	/**
	 * Get the current step.
	 * 
	 * @return  The current step in milliseconds.
	 */
	Long getStep();
	
	/**
	 * Get the current time.
	 * 
	 * @return  The current time in milliseconds.
	 */
	Double getTime();

}
