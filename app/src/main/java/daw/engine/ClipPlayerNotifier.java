package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;
import daw.general.MapToSet;

public class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	private final RPClock clock;
	
	private final MapToSet<Long, RPClipPlayer> observers;
	
	/**
	 * @param clock
	 * @param observers
	 */
	public ClipPlayerNotifier(RPClock clock, MapToSet<Long, RPClipPlayer> observers) {
		this.clock = clock;
		this.observers = observers;
	}

	@Override
	public void update() {
		Long step = this.clock.getStep();
		if(observers.containsKey(step)) {
			this.play(this.observers.get(step));
		}
	}

	private void play(Set<RPClipPlayer> set) {
		set.parallelStream().forEach(player->{
			player.play();
		});
	}

	@Override
	public void notifyStopped() {
		this.observers.entrySet().parallelStream().forEach(e->{
			e.getValue().parallelStream().forEach(p->{
				p.stop();
			});
		});
	}
	
	
}
