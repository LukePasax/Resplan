package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;

/**
 * Implementation of {@link RPClipPlayerNotifier}.
 */
public class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	/**
	 * The clock.
	 */
	private final RPClock clock;
	
	/**
	 * The players to notify.
	 */
	private final RPPlayersMap observers;
	
	/**
	 * Creates a clip player notifier with all the given observers subscribed.
	 * 
	 * @param  clock  The clock to read the current step from.
	 * 
	 * @param  observers  The clip players to register.
	 */
	public ClipPlayerNotifier(RPClock clock, RPPlayersMap observers) {
		this.clock = clock;
		this.observers = observers;
	}
	
	/**
	 * Creates a clip player notifier with no observers subscribed.
	 * 
	 * @param  clock  The clock to read the current step from
	 */
	public ClipPlayerNotifier(RPClock clock) {
		this(clock, new PlayersMap());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		Long step = this.clock.getStep();
		if(observers.containsStep(step)) {
			this.play(this.observers.getClipPlayersAt(step));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	private void play(Set<RPClipPlayer> set) {
		set.parallelStream().forEach(player->{
			player.play();
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyStopped() {
		this.observers.entrySet().parallelStream().forEach(e->{
			e.getValue().parallelStream().forEach(p->{
				p.stop();
			});
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObserver(Long step, RPClipPlayer clipPlayer) {
		return this.observers.putClipPlayer(step, clipPlayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObserver(Long step, RPClipPlayer clipPlayer) {
		return this.observers.removeClipPlayer(step, clipPlayer);
	}
}
