package daw.engine;

import java.util.Optional;
import daw.core.clip.RPClipPlayer;
import daw.core.clip.SampleClip;
import daw.core.clip.SampleClipPlayer;
import daw.manager.ChannelLinker;
import javafx.util.Pair;

public class Engine implements RPEngine {
	
	protected final static double CLOCK_STEP_UNIT = 0.02;
	
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
	private Optional<RPConductor> conductor;
	
	
	public Engine(ChannelLinker channelLinker) {
		this.channelLinker = channelLinker;
		this.clock = new Clock();
	}

	@Override
	public void start() {
		this.updateNotifier();
		// TODO 
		// crea ed avvia il thread Conductor
	}

	@Override
	public void pause() {
		// TODO stoppa il thread Conductor

	}

	@Override
	public void stop() {
		// TODO stoppa il thread Conductor
		// resetta il clock a zero
	}


	@Override
	public void setPlaybackTime(double time) {
		this.clock.setTime(time);
	}

	@Override
	public double getPlaybackTime() {
		return this.clock.getTime();
	}

	@Override
	public boolean isPaused() {
		// TODO se il thread è attivo
		return false;
	}
	
	private void updateNotifier() {
		MapToSet<Double, RPClipPlayer>  listeners = new HashMapToSet<>();
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
								listeners.put(0.0, player);
							} else {
								listeners.put(clip.getKey(), new SampleClipPlayer((SampleClip) clip.getValue()));
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
