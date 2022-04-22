package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;
import daw.general.MapToSet;

public class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	private final RPClock clock;
	
	private final MapToSet<Long, RPClipPlayer> listeners;
	
	/**
	 * @param clock
	 * @param listeners
	 */
	public ClipPlayerNotifier(RPClock clock, MapToSet<Long, RPClipPlayer> listeners) {
		this.clock = clock;
		this.listeners = listeners;
	}

	@Override
	public void update() {
		long time = this.clock.getTime();
		if(listeners.containsKey(time)) {
			this.play(this.listeners.get(time));
		}
	}

	private void play(Set<RPClipPlayer> set) {
		set.parallelStream().forEach(player->{
			player.play();
		});
	}

	@Override
	public void notifyStopped() {
		this.listeners.entrySet().parallelStream().forEach(e->{
			e.getValue().parallelStream().forEach(p->{
				p.stop();
			});
		});
	}
	
	
}
