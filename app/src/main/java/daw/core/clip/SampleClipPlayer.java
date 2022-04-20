package daw.core.clip;

import java.io.IOException;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.SamplePlayer;

public class SampleClipPlayer implements RPClipPlayer {
	
	private final SamplePlayer player;
	private final double contentPosition;
	
	public SampleClipPlayer(SampleClip sampleClip) throws IOException, OperationUnsupportedException, FileFormatException {
		this.player = new SamplePlayer(new Sample(sampleClip.getContent().getAbsolutePath()));
		this.contentPosition = sampleClip.getContentPosition();
		this.player.setPosition(this.contentPosition);
		this.stop();
	}

	@Override
	public void play() {
		this.player.start();
	}

	@Override
	public void pause() {
		this.player.pause(true);
	}

	@Override
	public void stop() {
		this.pause();
		this.setPlaybackPosition(contentPosition);
	}

	@Override
	public void setPlaybackPosition(double milliseconds) {
		this.player.setPosition(milliseconds);
	}

	@Override
	public double getPlaybackPosition() {
		return this.player.getPosition();
	}

}
