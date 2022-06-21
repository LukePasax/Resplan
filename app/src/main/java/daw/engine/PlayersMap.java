package daw.engine;

import java.util.Map.Entry;
import java.util.Set;
import daw.core.clip.RPClipPlayer;
import daw.utilities.HashMapToSet;
import daw.utilities.MapToSet;

/**
 * Implementation of {@link RPPlayersMap}. It wraps a {@link MapToSet}.
 */
public final class PlayersMap implements RPPlayersMap {
	
	private final MapToSet<Long, RPClipPlayer>  observers = new HashMapToSet<>();

	@Override
	public boolean putClipPlayer(final Long step, final RPClipPlayer clipPlayer) {
		return this.observers.put(step, clipPlayer);
	}

	@Override
	public boolean removeClipPlayer(final Long step, final RPClipPlayer clipPlayer) {
		return this.observers.remove(step, clipPlayer);
	}

	@Override
	public Set<RPClipPlayer> removeClipPlayersAt(final Long step) {
		return this.observers.removeSet(step);
	}

	@Override
	public Set<RPClipPlayer> getClipPlayersAt(final Long step) {
		return this.observers.get(step);
	}

	@Override
	public boolean containsStep(final Long step) {
		return this.observers.containsKey(step);
	}

	@Override
	public boolean isEmpty() {
		return this.observers.isEmpty();
	}

	@Override
	public Set<Entry<Long, Set<RPClipPlayer>>> entrySet() {
		return this.observers.entrySet();
	}
}
