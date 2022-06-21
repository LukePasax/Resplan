package daw.engine;

import java.util.Optional;
import daw.manager.ChannelLinker;

/**
 * Implementation of {@link RPEngine}.
 * <p>A few objects act to make the engine run:
 * <ol>
 * <li>In order to start the playback, the engine gets an {@link RPPlayersMap} from 
 * a {@link PlayersMapBuilder}.
 * <li>The produced players map will be used from an {@link RPClipPlayerNotifier} 
 * to register all the players to be notified.
 * <li>The engine use an {@link RPClock} to play each clip at the right time.
 * <li>A new {@link Conductor} updates the clock and the notifier 
 * every {@code Clock.CLOCK_STEP_UNIT}.
 * <li>Every time the clip player notifier is updated from the conductor the players 
 * subscribed to the current clock step will be notified.
 * </ol>
 */
public final class Engine implements RPEngine {
	
	/**
	 * The ChannelLinker to get the channels and clips from.
	 */
	private final ChannelLinker channelLinker;
	
	/**
	 * The clock.
	 */
	private final RPClock clock;
	
	/**
	 * The player notifier.
	 */
	private Optional<RPClipPlayerNotifier> notifier;
	
	/**
	 * The thread whitch updates clock ad notifier every CLOCK_STEP_UNIT.
	 */
	private Optional<Conductor> conductor;

	public Engine(final ChannelLinker channelLinker) {
		this.channelLinker = channelLinker;
		this.conductor = Optional.empty();
		this.clock = new Clock();
	}

	@Override
	public void start() {
		if (isPaused()) {
			this.updateObservers();
			this.conductor = Optional.of(new Conductor(notifier.get(), clock));
			this.conductor.get().start();
		}
	}

	@Override
	public void pause() {
		if (!isPaused()) {
			this.conductor.get().notifyStopped();
			this.conductor = Optional.empty();
			this.notifier.get().notifyStopped();
			this.notifier = Optional.empty();
		}
	}

	@Override
	public void stop() {
		this.pause();
		this.clock.reset();
		this.conductor = Optional.empty();
	}

	@Override
	public void setPlaybackTime(final Double time) {
		this.clock.setTime(time);
	}

	@Override
	public Double getPlaybackTime() {
		return this.clock.getTime();
	}

	@Override
	public boolean isPaused() {
		return this.conductor.isEmpty();
	}
	
	private void updateObservers() {
		this.notifier = Optional.of(new ClipPlayerNotifier(
			new PlayersMapBuilderImpl().setChannelLinker(channelLinker)
				.addSampleClipsBetween(Optional.of(this.getPlaybackTime()), Optional.empty())
				.build()
		));
	}
}
