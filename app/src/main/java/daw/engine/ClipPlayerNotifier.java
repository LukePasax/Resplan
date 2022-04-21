package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;

public class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	private final RPClock clock;
	
	private final MapToSet<Double, RPClipPlayer> listeners;
	
	/**
	 * @param clock
	 * @param listeners
	 */
	public ClipPlayerNotifier(RPClock clock, MapToSet<Double, RPClipPlayer> listeners) {
		this.clock = clock;
		this.listeners = listeners;
	}

	@Override
	public void update() {
		double time = this.clock.getTime();
		if(listeners.containsKey(time)) {
			this.play(this.listeners.get(time));
		}
	}

	private void play(Set<RPClipPlayer> set) {
		set.parallelStream().forEach(player->{
			player.play();
		});
	}
}
