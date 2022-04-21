package daw.engine;

import java.util.Set;

public interface MapToSet<X,Y> {
	
	boolean put(X key, Y value);
	
	boolean remove(X key, Y value);
	
	Set<Y> removeSet(X key);
	
	Set<Y> get(X key);

	boolean containsKey(X time);

}
