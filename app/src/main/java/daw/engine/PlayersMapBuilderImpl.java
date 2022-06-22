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
public final class PlayersMapBuilderImpl implements PlayersMapBuilder {
	
	/**
	 * The players map.
	 */
	private RPPlayersMap playersMap = new PlayersMap();
	
	/**
	 * The channel linker.
	 */
	private Optional<ChannelLinker> channelLinker = Optional.empty();
	
	/**
	 * The sample player factory.
	 */
	private final ClipPlayerFactory samplePlayerFactory = new SampleClipPlayerFactory();

	@Override
	public RPPlayersMap build() {
		RPPlayersMap current = this.playersMap;
		this.playersMap = new PlayersMap();
		return current;
	}

	@Override
	public PlayersMapBuilder setChannelLinker(final ChannelLinker channelLinker) {
		this.channelLinker = Optional.of(channelLinker);
		return this;
	}

	@Override
	public PlayersMapBuilder addSampleClipsBetween(final Optional<Double> timeIn, final Optional<Double> timeOut) {
		if (this.channelLinker.isEmpty()) {
			throw new IllegalStateException("Can't find a channelLinker source. "
					+ "Call setChannelLinker(ChannelLinker cl) before of this method.");
		}
		double in = timeIn.isEmpty() ? 0.0 : timeIn.get();
		double out = timeOut.isEmpty() ? Clock.Utility.getClockMaxTime() : timeIn.get();
		this.channelLinker.get().getAudioSet().forEach(channel -> {
			//get iterator of all SampleClips after timeIn and before timeOut
			var clipIterator = ((TapeChannel) channel.getValue()).
					getClipWithTimeIteratorFiltered(x -> x.getValue().getClass().equals(SampleClip.class)
							&& channel.getValue().calculateTimeOut(x.getKey(), x.getValue().getDuration()) > in
							&& x.getKey() < out);
			clipIterator.forEachRemaining(clip -> {
				try {
					//gestisco eventuali clip che partono a metà del time in
					if (clip.getKey() < in) {
						this.playersMap.putClipPlayer(Clock.Utility.timeToClockSteps(in),
								this.samplePlayerFactory.createClipPlayerWithActiveCut(clip.getValue(),
										channel.getKey(), in - clip.getKey()));
					} else {
						this.playersMap.putClipPlayer(Clock.Utility.timeToClockSteps(clip.getKey()),
								this.samplePlayerFactory.createClipPlayer(clip.getValue(),
										channel.getKey()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
		return this;	
	}
}
