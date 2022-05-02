package daw.engine;

public interface RPClock {
	
	void step() throws ClockException;
	
	void reset();
	
	void setTime(Double time);
	
	Long getStep();
	
	Double getTime();

}
