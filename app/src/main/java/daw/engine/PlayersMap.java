package daw.engine;

import java.util.Map.Entry;
import java.util.Set;

import daw.core.clip.RPClipPlayer;
import daw.general.HashMapToSet;
import daw.general.MapToSet;

public class PlayersMap implements RPPlayersMap {
	
	private final MapToSet<Long, RPClipPlayer>  observers = new HashMapToSet<>();

	@Override
	public boolean putClipPlayer(Long step, RPClipPlayer clipPlayer) {
		return this.observers.put(step, clipPlayer);
	}

	@Override
	public boolean removeClipPlayer(Long step, RPClipPlayer clipPlayer) {
		return this.observers.remove(step, clipPlayer);
	}

	@Override
	public Set<RPClipPlayer> removeClipPlayersAt(Long step) {
		return this.observers.removeSet(step);
	}

	@Override
	public Set<RPClipPlayer> getClipPlayersAt(Long step) {
		return this.observers.get(step);
	}

	@Override
	public boolean containsStep(Long step) {
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
