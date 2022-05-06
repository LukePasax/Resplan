package daw.general;

import java.util.Set;
import java.util.Map.Entry;

public interface MapToSet<X,Y> {
	
	boolean put(X key, Y value);
	
	boolean remove(X key, Y value);
	
	Set<Y> removeSet(X key);
	
	Set<Y> get(X key);

	boolean containsKey(X Key);
	
	boolean isEmpty();
	
	Set<Entry<X,Set<Y>>> entrySet();

}
