package daw.engine;

import java.util.Map.Entry;
import java.util.Set;

import daw.core.clip.RPClipPlayer;
import daw.utilities.HashMapToSet;
import daw.utilities.MapToSet;

/**
 * Implementation of {@link RPPlayersMap}. It wraps a {@link MapToSet}.
 */
public class PlayersMap implements RPPlayersMap {
	
	private final MapToSet<Long, RPClipPlayer>  observers = new HashMapToSet<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean putClipPlayer(Long step, RPClipPlayer clipPlayer) {
		return this.observers.put(step, clipPlayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeClipPlayer(Long step, RPClipPlayer clipPlayer) {
		return this.observers.remove(step, clipPlayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<RPClipPlayer> removeClipPlayersAt(Long step) {
		return this.observers.removeSet(step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<RPClipPlayer> getClipPlayersAt(Long step) {
		return this.observers.get(step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsStep(Long step) {
		return this.observers.containsKey(step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.observers.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Entry<Long, Set<RPClipPlayer>>> entrySet() {
		return this.observers.entrySet();
	}
}
