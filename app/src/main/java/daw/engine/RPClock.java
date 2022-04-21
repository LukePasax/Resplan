package daw.engine;

public interface RPClock {
	
	void step();
	
	void reset();
	
	void setTime(double time);
	
	double getTime();

}
