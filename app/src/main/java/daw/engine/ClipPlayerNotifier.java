package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;

/**
 * Implementation of {@link RPClipPlayerNotifier}.
 */
public class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	private final RPPlayersMap toStop = new PlayersMap();
	
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
	public ClipPlayerNotifier(RPPlayersMap observers) {
		this.observers = observers;
	}
	
	/**
	 * Creates a clip player notifier with no observers subscribed.
	 * 
	 * @param  clock  The clock to read the current step from
	 */
	public ClipPlayerNotifier() {
		this(new PlayersMap());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Long step) {
		observers.entrySet().stream().filter(entry->{
			return entry.getKey()<=step;
		}).forEach(entry->{
			//play
			this.play(entry.getValue());
			//add stop observer
			entry.getValue().forEach(player->{
				addToStop(entry.getKey(), player);
			});
		});
		toStop.entrySet().stream().filter(entry->{
			return entry.getKey()<=step;
		}).forEach(entry->{
			//stop
			this.stop(entry.getValue());
		});
	}

	/**
	 * {@inheritDoc}
	 */
	private void play(Set<RPClipPlayer> set) {
		set.stream().forEach(player->{
			player.play();
		});
	}
	
	private void stop(Set<RPClipPlayer> set) {
		set.stream().forEach(player->{
			player.stop();
		});
	}

	private void addToStop(Long step, RPClipPlayer player) {
		toStop.putClipPlayer(step+Clock.Utility.timeToClockSteps(player.getPlaybackDuration()), player);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyStopped() {
		this.observers.entrySet().stream().forEach(e->{
			stop(e.getValue());
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
