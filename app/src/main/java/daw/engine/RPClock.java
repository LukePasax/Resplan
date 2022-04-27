package daw.engine;

public interface RPClock {
	
	void step() throws ClockException;
	
	void reset();
	
	void setTime(Double time);
	
	Long getStep();
	
	Double getTime();
	
	Long timeToClockSteps(Double time);
	
	Double clockStepToTime(Long clockStep);
	
	Double getClockStepUnit();
	
	Double getClockMaxTime();
	
	Long getClockMaxStep();
	
	Double roundToExistingClockTime(Double time);

}
