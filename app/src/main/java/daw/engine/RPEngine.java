package daw.engine;

public interface RPEngine {
	
	void start();
	
	void pause();
	
	void stop();
	
	void setPlaybackTime(double time);
	
	double getPlaybackTime();
	
	boolean isPaused();

}
