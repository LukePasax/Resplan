package daw.engine;

public interface RPClock {
	
	void step();
	
	void reset();
	
	void setTime(long time);
	
	long getTime();

}
