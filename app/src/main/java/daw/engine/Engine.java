package daw.engine;

import java.util.Optional;

import daw.core.clip.ClipPlayerFactory;
import daw.core.clip.RPClipPlayer;
import daw.core.clip.SampleClip;
import daw.core.clip.SampleClipPlayerFactory;
import daw.general.HashMapToSet;
import daw.general.MapToSet;
import daw.manager.ChannelLinker;
import javafx.util.Pair;

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
	private ClipPlayerFactory SamplePlayerFactory = new SampleClipPlayerFactory();
	
	
	public Engine(ChannelLinker channelLinker) {
		this.channelLinker = channelLinker;
		this.clock = new Clock();
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
		MapToSet<Long, RPClipPlayer>  observers = new HashMapToSet<>();
//CREAZIONE OBSERVERS
		//for each channel
		this.channelLinker.getAudioSet().forEach(channel->{
			//get SampleClips after actual playback time
			var clipIterator = channel.getValue().getClipWithTimeIteratorFiltered(x->{
				return x.getValue().getClass().equals(SampleClip.class) 
						&& channel.getValue().getClipTimeOut(new Pair<>(x.getKey(), x.getValue()))>this.getPlaybackTime();
			});
			//for each clip of the channel
			clipIterator.forEachRemaining(clip->{
				try {
					//gestisco eventuali clip che partono a metà
					if(clip.getKey()<this.getPlaybackTime()) {
						observers.put(0l, this.SamplePlayerFactory.createClipPlayerWithActiveCut(clip.getValue(), channel.getKey(), this.getPlaybackTime()-clip.getKey()));
					} else {
						observers.put(clock.timeToClockSteps(clip.getKey()), this.SamplePlayerFactory.createClipPlayer(clip.getValue(), channel.getKey()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
		
//AGGIORNAMENTO NOTIFIER
		this.notifier = Optional.of(new ClipPlayerNotifier(this.clock, observers));	
	}
}
