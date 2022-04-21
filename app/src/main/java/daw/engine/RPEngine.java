package daw.engine;

public interface RPEngine {
	
	void start();
	
	void pause();
	
	void stop();
	
	void setPlaybackTime(long time);
	
	long getPlaybackTime();
	
	boolean isPaused();

}
