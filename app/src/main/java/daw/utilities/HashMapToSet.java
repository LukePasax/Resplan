package daw.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class HashMapToSet<X, Y> implements MapToSet<X, Y> {
	
	private final Map<X, Set<Y>> map = new HashMap<>();

	@Override
	public boolean put(final X key, final Y value) {
		if (!map.containsKey(key)) {
			map.put(key, new HashSet<>());
		}
		return map.get(key).add(value);
	}

	@Override
	public boolean remove(final X key, final Y value) {
		if (map.containsKey(key)) {
			final var removed = map.get(key).remove(value);
			if (map.get(key).isEmpty() && removed) {
				return this.removeSet(key) != null;
			}
			return removed;
		} 
		return false;
	}

	@Override
	public Set<Y> removeSet(final X key) {
		 return map.remove(key);
	}

	@Override
	public Set<Y> get(final X key) {
		return map.get(key);
	}

	@Override
	public boolean containsKey(final X time) {
		return this.map.containsKey(time);
	}
	
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	@Override
	public Set<Entry<X, Set<Y>>> entrySet() {
		return this.map.entrySet();
	}
}
