package daw.engine;

public interface RPEngine {
	
	void start();
	
	void pause();
	
	void stop();
	
	void setPlaybackTime(Double time);
	
	Double getPlaybackTime();
	
	boolean isPaused();

}
