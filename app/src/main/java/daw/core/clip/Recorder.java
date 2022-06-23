package daw.core.clip;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.RecordToSample;

/**
 * Implementation of {@link RPRecorder}.
 *
 */
public final class Recorder implements RPRecorder {

	/**
	 * The audio context.
	 */
	private final AudioContext ac;
	
	/**
	 * The audio input.
	 */
	private final UGen input;
	
	/**
	 * The recorded {@link Sample}.
	 */
	private final Sample recordedData;
	
	/**
	 * The {@code beads} recorder.
	 */
	private final RecordToSample recorder;
	
	/**
	 * Create a new {@code Recorder} connected to the default system audio input.
	 */
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
		ac.start();
		recorder.start();
	}

	@Override
	public void pause() {
		recorder.pause(true);
		ac.stop();
	}

	@Override
	public void reset() {
		recordedData.clear();
	}

	@Override
	public Sample getSample() {
		recorder.clip();
		return this.recordedData;
	}
}
