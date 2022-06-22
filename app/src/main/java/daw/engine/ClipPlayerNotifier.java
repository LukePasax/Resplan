package daw.engine;

import java.util.Set;
import daw.core.clip.RPClipPlayer;

/**
 * Implementation of {@link RPClipPlayerNotifier}.
 */
public final class ClipPlayerNotifier implements RPClipPlayerNotifier {
	
	private final RPPlayersMap toStop = new PlayersMap();
	
	private Long oldStep = -1L;
	
	/**
	 * The players to notify.
	 */
	private final RPPlayersMap toPlay;
	
	/**
	 * Creates a clip player notifier with all the given observers subscribed.
	 * 
	 * @param  observers  The clip players to register.
	 */
	public ClipPlayerNotifier(final RPPlayersMap observers) {
		this.toPlay = observers;
	}
	
	/**
	 * Creates a clip player notifier with no observers subscribed.
	 */
	public ClipPlayerNotifier() {
		this(new PlayersMap());
	}

	@Override
	public void update(final Long step) {
		toPlay.entrySet().stream().filter(entry -> {
			return entry.getKey() <= step && entry.getKey() > oldStep;
		}).forEach(entry -> {
			//play
			this.play(entry.getValue());
			//add stop observer
			entry.getValue().forEach(player -> {
				addToStop(entry.getKey(), player);
			});
		});
		toStop.entrySet().stream().filter(entry -> {
			return entry.getKey() <= step && entry.getKey() > oldStep;
		}).forEach(entry -> {
			//stop
			this.stop(entry.getValue());
		});
		oldStep = step;
	}

	private void play(final Set<RPClipPlayer> set) {
		set.stream().forEach(player -> {
			player.play();
		});
	}
	
	private void stop(final Set<RPClipPlayer> set) {
		set.stream().forEach(player -> {
			player.stop();
		});
	}

	private void addToStop(final Long step, final RPClipPlayer player) {
		Double cutTime = player.isCutActive() ? player.getCutTime() : 0.0;
		toStop.putClipPlayer(step + Clock.Utility.timeToClockSteps(player.getPlaybackDuration() - cutTime), player);
	}

	@Override
	public void notifyStopped() {
		this.toPlay.entrySet().stream().forEach(e -> {
			stop(e.getValue());
		});
	}

	@Override
	public boolean addObserver(final Long step, final RPClipPlayer clipPlayer) {
		return this.toPlay.putClipPlayer(step, clipPlayer);
	}

	@Override
	public boolean removeObserver(final Long step, final RPClipPlayer clipPlayer) {
		return this.toPlay.removeClipPlayer(step, clipPlayer);
	}
}
