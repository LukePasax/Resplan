package daw.core.clip;

import java.util.Optional;

import daw.core.channel.RPChannel;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.LoopType;

/**
 * A Factory for {@link RPClipPlayer} which plays a {@link SampleClip}
 *	<p>Creates a {@link SampleClipPlayer} and connect it with the given {@link RPChannel}.
 *	Could also create a player with an active cut already setted.
 */
public class SampleClipPlayerFactory implements ClipPlayerFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RPClipPlayer createClipPlayer(RPClip<?> clip, RPChannel channel) {
		if(!clip.getClass().equals(SampleClip.class)) {
			throw new IllegalArgumentException("The supplied clip must be a Sample Clip");
		}
		SampleClipPlayer player = new SampleClipPlayer((SampleClip) clip);
		channel.connectSource(player.getUGen());
		return player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RPClipPlayer createClipPlayerWithActiveCut(RPClip<?> clip, RPChannel channel, double cut) {
		var player = this.createClipPlayer(clip, channel);
		player.setCut(cut);
		return player;
	}
	
	/**
	 * An {@link RPClipPlayer} for a {@link SampleClip}.
	 *
	 */
	public class SampleClipPlayer implements RPClipPlayer {
		
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
		private SampleClipPlayer(SampleClip sampleClip) {
			this.player = new SamplePlayer(sampleClip.getContent());
			this.clip = sampleClip;
			this.player.setPosition(this.clip.getContentPosition());
			this.player.setLoopType(LoopType.NO_LOOP_FORWARDS);
			this.cutTime = Optional.empty();
			this.stop();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void play() {
			this.player.start();
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		public void pause() {
			this.player.pause(true);
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		public void stop() {
			this.pause();
			if(cutTime.isPresent()) {
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
		public void setPlaybackPosition(double milliseconds) {
			if(milliseconds<=0 || milliseconds>=clip.getDuration()) {
				throw new IllegalArgumentException("The playback position must be a non-zero and positive value.");
			}
			this.player.setPosition(milliseconds+clip.getContentPosition());
		}

		/**
		 *  {@inheritDoc}
		 */
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
		public void setCut(double time) {
			if(time<=0) {
				throw new IllegalArgumentException("The supplied time must be a non-zero and positive value.");
			}
			this.cutTime = Optional.of(time);
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		public void disableCut() {
			this.cutTime = Optional.empty();
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		public UGen getUGen() {
			return this.player;
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		public boolean isPaused() {
			return this.player.isPaused();
		}

		/**
		 *  {@inheritDoc}
		 */
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
			if(this.cutTime.isEmpty()) {
				throw new IllegalStateException("The cut is not active for this player");
			}
			return this.cutTime.get();
		}
	}


}
