package daw.core.clip;

import java.util.Optional;
import daw.core.channel.RPChannel;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.LoopType;

/**
 * A Factory for {@link RPClipPlayer} which plays a {@link SampleClip}
 *	<p>Creates a {@link SampleClipPlayer} and connect it with the given {@link RPChannel}.
 *	Could also create a player with an active cut already set.
 */
public final class SampleClipPlayerFactory implements ClipPlayerFactory {

	@Override
	public RPClipPlayer createClipPlayer(final RPClip<?> clip, final RPChannel channel) {
		if (!clip.getClass().equals(SampleClip.class)) {
			throw new IllegalArgumentException("The supplied clip must be a Sample Clip");
		}
		SampleClipPlayer player = new SampleClipPlayer((SampleClip) clip);
		channel.connectSource(player.getUGen());
		return player;
	}

	@Override
	public RPClipPlayer createClipPlayerWithActiveCut(final RPClip<?> clip, final RPChannel channel, final double cut) {
		var player = this.createClipPlayer(clip, channel);
		player.setCut(cut);
		return player;
	}
	
	/**
	 * An {@link RPClipPlayer} for a {@link SampleClip}.
	 */
	public static final class SampleClipPlayer implements RPClipPlayer {

		/**
		 * The wrapped UGen.
		 */
		private final SamplePlayer player;

		/**
		 * The RPClip to play.
		 */
		private final RPClip<Sample> clip;

		/**
		 * The optional cutTime.
		 */
		private Optional<Double> cutTime;

		/**
		 * Creates a SampleClipPlayer from a sampleClip.
		 * 
		 * @param  sampleClip  The {@link SampleClip} to play.
		 */
		private SampleClipPlayer(final SampleClip sampleClip) {
			this.player = new SamplePlayer(AudioContextManager.getAudioContext(), sampleClip.getContent());
			this.player.setLoopType(LoopType.NO_LOOP_FORWARDS);
			this.clip = sampleClip;
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
			if (cutTime.isPresent()) {
				this.setPlaybackPosition(cutTime.get());
			} else {
				this.setPlaybackPosition(0);
			}	
		}

		/**
		 *  {@inheritDoc}
		 *
		 *  @throws  IllegalArgumentException  {@inheritDoc}
		 */
		@Override
		public void setPlaybackPosition(final double milliseconds) {
			if (milliseconds < 0 || milliseconds >= clip.getDuration()) {
				throw new IllegalArgumentException("The playback position must be a positive value.");
			}
			this.player.setPosition(milliseconds + clip.getContentPosition());
		}

		@Override
		public double getPlaybackPosition() {
			return this.player.getPosition();
		}

		/**
		 *  {@inheritDoc}
		 *
		 *  @throws  IllegalArgumentException  {@inheritDoc}
		 */
		@Override
		public void setCut(final double time) {
			if (time <= 0) {
				throw new IllegalArgumentException("The supplied time must be a non-zero and positive value.");
			}
			this.cutTime = Optional.of(time);
			this.setPlaybackPosition(cutTime.get());
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

		/**
		 *  {@inheritDoc}
		 *
		 *  @throws  IllegalStateException  {@inheritDoc}
		 */
		@Override
		public double getCutTime() {
			if (this.cutTime.isEmpty()) {
				throw new IllegalStateException("The cut is not active for this player");
			}
			return this.cutTime.get();
		}

		@Override
		public double getPlaybackDuration() {
			return this.clip.getDuration();
		}
	}
}
