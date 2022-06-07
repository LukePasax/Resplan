package daw.core.clip;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.RecordToSample;

public class Recorder implements RPRecorder {
	
	AudioContext ac;
	UGen input;
	Sample recordedData;
	RecordToSample recorder;
	
	public Recorder() {
		ac = AudioContextManager.getAudioContext();
		input = ac.getAudioInput();
		recordedData = new Sample(0);
		recorder = new RecordToSample(ac, recordedData, RecordToSample.Mode.INFINITE);
		recorder.addInput(input);
		ac.out.addDependent(recorder);
		recorder.pause(true);
		recordedData.clear();
	}

	@Override
	public void record() {
		recorder.start();
	}

	@Override
	public void pause() {
		recorder.pause(true);
		recorder.clip();
	}

	@Override
	public void reset() {
		recordedData.clear();
	}

	@Override
	public Sample getSample() {
		return this.recordedData;
	}

}
