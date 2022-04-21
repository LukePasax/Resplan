package daw.engine;

import java.util.Optional;
import daw.core.clip.RPClipPlayer;
import daw.core.clip.SampleClip;
import daw.core.clip.SampleClipPlayer;
import daw.manager.ChannelLinker;
import javafx.util.Pair;

public class Engine implements RPEngine {
	
	protected final static long CLOCK_STEP_UNIT = 1;
	
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
	
	
	public Engine(ChannelLinker channelLinker) {
		this.channelLinker = channelLinker;
		this.clock = new Clock();
	}

	@Override
	public void start() {
		this.updateNotifier();
		this.conductor = Optional.of(new Conductor(notifier.get(), clock));
		this.conductor.get().start();
	}

	@Override
	public void pause() {
		this.conductor.get().notifyStopped();
	}

	@Override
	public void stop() {
		// TODO stoppa il thread Conductor
		// resetta il clock a zero
	}


	@Override
	public void setPlaybackTime(long time) {
		this.clock.setTime(time);
	}

	@Override
	public long getPlaybackTime() {
		return this.clock.getTime();
	}

	@Override
	public boolean isPaused() {
		// TODO se il thread è attivo
		return false;
	}
	
	private void updateNotifier() {
		MapToSet<Long, RPClipPlayer>  listeners = new HashMapToSet<>();
		//for each channel
		this.channelLinker.getAudioSet().forEach(channel->{
			//get SampleClips after actual playback time
			var clipIterator = channel.getValue().getClipWithTimeIteratorFiltered(x->{
				return x.getValue().getClass().equals(SampleClip.class) 
						&& channel.getValue().getClipTimeOut(new Pair<>(x.getKey(), x.getValue()))>this.getPlaybackTime();
			});
			//if there are clips
			if(clipIterator.isPresent()) {
				//for each clip of the channel
				channel.getValue().getClipWithTimeIterator().get().forEachRemaining(clip->{
						try {
							//gestisco eventuali clip che partono a metà
							if(clip.getKey()<this.getPlaybackTime()) {
								var player = new SampleClipPlayer((SampleClip) clip.getValue());
								player.setCut(this.getPlaybackTime()-clip.getKey());
								channel.getKey().connectSource(player.getUGen());
								listeners.put(0l, player);
							} else {
								var player = new SampleClipPlayer((SampleClip) clip.getValue());
								listeners.put(clip.getKey().longValue(), player);
								channel.getKey().connectSource(player.getUGen());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
				});
			};
		});
		this.notifier = Optional.of(new ClipPlayerNotifier(this.clock, listeners));	
	}
}
