package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;

public class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	private final RPClock clock;
	
	private final RPPlayersMap observers;
	
	/**
	 * @param clock
	 * @param observers
	 */
	public ClipPlayerNotifier(RPClock clock, RPPlayersMap observers) {
		this.clock = clock;
		this.observers = observers;
	}

	@Override
	public void update() {
		Long step = this.clock.getStep();
		if(observers.containsStep(step)) {
			this.play(this.observers.getClipPlayersAt(step));
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
