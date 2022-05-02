package daw.engine;

import java.util.Optional;

import daw.manager.ChannelLinker;

public class Engine implements RPEngine {
	
	/**
	 * The ChannelLinker to get the channels and clips from.
	 */
	private final ChannelLinker channelLinker;
	
	/**
	 * The clock
	 */
	private final RPClock clock;
	
	/**
	 * The Notifier that notify all the players.
	 */
	private Optional<RPClipPlayerNotifier> notifier;
	
	/**
	 * The thread whitch updates clock ad notifier every CLOCK_STEP_UNIT.
	 */
	private Optional<Conductor> conductor;
	
	/**
	 * 
	 */
	private final PlayersMapBuilder playersMapBuilder;
		
	public Engine(ChannelLinker channelLinker) {
		this.channelLinker = channelLinker;
		this.clock = new Clock();
		this.playersMapBuilder = new PlayersMapBuilderImpl();
	}

	@Override
	public void start() {
		this.updateObservers();
		this.conductor = Optional.of(new Conductor(notifier.get(), clock));
		this.conductor.get().start();
	}

	@Override
	public void pause() {
		this.conductor.get().notifyStopped();
		this.conductor = Optional.empty();
	}

	@Override
	public void stop() {
		this.pause();
		this.clock.reset();
		this.conductor = Optional.empty();
	}


	@Override
	public void setPlaybackTime(Double time) {
		this.clock.setTime(time);
	}

	@Override
	public Double getPlaybackTime() {
		return this.clock.getTime();
	}

	@Override
	public boolean isPaused() {
		return this.conductor.isPresent();
	}
	
	private void updateObservers() {
		this.notifier = Optional.of(new ClipPlayerNotifier(
			this.clock, 
			this.playersMapBuilder.setChannelLinker(channelLinker)
				.addSampleClipsBetween(Optional.of(this.getPlaybackTime()), Optional.empty())
				.build()
		));
	}
}
