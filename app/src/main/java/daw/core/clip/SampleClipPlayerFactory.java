package daw.core.clip;

import java.io.IOException;
import java.util.Optional;

import daw.core.channel.RPChannel;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.LoopType;

public class SampleClipPlayerFactory implements ClipPlayerFactory {

	@Override
	public RPClipPlayer createClipPlayer(RPClip clip, RPChannel channel) throws IOException, OperationUnsupportedException, FileFormatException {
		if(!clip.getClass().equals(SampleClip.class)) {
			throw new IllegalArgumentException("The supplied clip must be a Sample Clip");
		}
		SampleClipPlayer player = new SampleClipPlayer((SampleClip) clip);
		channel.connectSource(player.getUGen());
		return player;
	}

	@Override
	public RPClipPlayer createClipPlayerWithActiveCut(RPClip clip, RPChannel channel, double cut) throws IOException, OperationUnsupportedException, FileFormatException {
		var player = this.createClipPlayer(clip, channel);
		player.setCut(cut);
		return player;
	}
	
	public class SampleClipPlayer implements RPClipPlayer {
		
		/**
		 * The wrapped UGen.
		 */
		private final SamplePlayer player;
		
		/**
		 * The content position of the RPClip to play.
		 */
		private final double contentPosition;
		
		/**
		 * The optional cutTime.
		 */
		private Optional<Double> cutTime;
		
		/**
		 * Creates a SampleClipPlayer from a sampleClip.
		 * @param sampleClip
		 * @throws IOException
		 * @throws OperationUnsupportedException
		 * @throws FileFormatException
		 */
		private SampleClipPlayer(SampleClip sampleClip) throws IOException, OperationUnsupportedException, FileFormatException {
			this.player = new SamplePlayer(new Sample(sampleClip.getContent().getAbsolutePath()));
			this.contentPosition = sampleClip.getContentPosition();
			this.player.setPosition(this.contentPosition);
			this.player.setLoopType(LoopType.NO_LOOP_FORWARDS);
			this.cutTime = Optional.empty();
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
			if(cutTime.isPresent()) {
				this.setPlaybackPosition(cutTime.get());
			} else {
				this.setPlaybackPosition(0);
			}	
		}

		@Override
		public void setPlaybackPosition(double milliseconds) {
			this.player.setPosition(milliseconds+contentPosition);
		}

		@Override
		public double getPlaybackPosition() {
			return this.player.getPosition();
		}

		@Override
		public void setCut(double time) {
			if(time<=0) {
				throw new IllegalArgumentException("The supplied time could not be zero or a negative value.");
			}
			this.cutTime = Optional.of(time);
		}

		@Override
		public void disableCut() {
			this.cutTime = Optional.empty();
		}

		@Override
		public UGen getUGen() {
			return this.player;
		}

		@Override
		public boolean isPaused() {
			return this.player.isPaused();
		}

		@Override
		public boolean isCutActive() {
			return this.cutTime.isPresent();
		}

		@Override
		public double getCutTime() {
			if(this.cutTime.isEmpty()) {
				throw new IllegalStateException("The cut is not active for this player");
			}
			return this.cutTime.get();
		}

	}


}
