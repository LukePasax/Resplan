package daw.engine;

import java.util.Optional;

import daw.core.channel.RPChannel;
import daw.core.clip.ClipPlayerFactory;
import daw.core.clip.SampleClip;
import daw.core.clip.SampleClipPlayerFactory;
import daw.core.clip.TapeChannel;
import daw.manager.ChannelLinker;

/**
 * Implementation of {@link PlayersMapBuilder}.
 */
public class PlayersMapBuilderImpl implements PlayersMapBuilder {
	
	private RPPlayersMap playersMap = new PlayersMap();
	
	private Optional<ChannelLinker> channelLinker = Optional.empty();
	
	
	private ClipPlayerFactory SamplePlayerFactory = new SampleClipPlayerFactory();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RPPlayersMap build() {
		RPPlayersMap current = this.playersMap;
		this.playersMap = new PlayersMap();
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayersMapBuilder setChannelLinker(ChannelLinker channelLinker) {
		this.channelLinker = Optional.of(channelLinker);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayersMapBuilder addSampleClipsBetween(Optional<Double> timeIn, Optional<Double> timeOut) {
		if(this.channelLinker.isEmpty()) {
			throw new IllegalStateException("Can't find a channelLinker source. " +
					"Call setChannelLinker(ChannelLinker cl) before of this method.");
		}
		
		double in = timeIn.isEmpty() ? 0.0 : timeIn.get();
		double out = timeOut.isEmpty() ? Clock.Utility.getClockMaxTime() : timeIn.get();

		this.channelLinker.get().getAudioSet().forEach(channel->{
			//get iterator of all SampleClips after timeIn and before timeOut
			var clipIterator = ((TapeChannel) channel.getValue()).
					getClipWithTimeIteratorFiltered(x-> x.getValue().getClass().equals(SampleClip.class) &&
							((TapeChannel) channel.getValue()).calculateTimeOut(x.getKey(), x.getValue().getDuration())>in &&
							x.getKey()<out);
			clipIterator.forEachRemaining(clip->{
				try {
					//gestisco eventuali clip che partono a metà del time in
					if(clip.getKey()<in) {
						this.playersMap.putClipPlayer(Clock.Utility.timeToClockSteps(in),
								this.SamplePlayerFactory.createClipPlayerWithActiveCut(clip.getValue(),
										(RPChannel) channel.getKey(), in-clip.getKey()));
					} else {
						this.playersMap.putClipPlayer(Clock.Utility.timeToClockSteps(clip.getKey()),
								this.SamplePlayerFactory.createClipPlayer(clip.getValue(),
										(RPChannel) channel.getKey()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
		return this;	
	}

}
