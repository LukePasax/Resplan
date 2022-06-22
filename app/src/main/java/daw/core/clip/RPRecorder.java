package daw.core.clip;

import net.beadsproject.beads.data.Sample;

/**
 * A simple recorder. 
 * Records an audio stream from the default system input and get the 
 * recorded {@link Sample}.
 */
public interface RPRecorder {
	
	/**
	 * Start to record on a new {@link Sample}.
	 */
	void record();
	
	/**
	 * Pause the recording.
	 */
	void pause();
	
	/**
	 * Delete all the recorded data and reset the recorder.
	 */
	void reset();
	
	/**
	 * Get the recorded {@link Sample}.
	 * 
	 * @return  the recorded sample.
	 */
	Sample getSample();

}
