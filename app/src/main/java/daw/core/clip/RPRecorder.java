package daw.core.clip;

import net.beadsproject.beads.data.Sample;

public interface RPRecorder {
	
	void record();
	
	void pause();
	
	void reset();
	
	Sample getSample();

}
