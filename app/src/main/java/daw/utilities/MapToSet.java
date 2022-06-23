package daw.utilities;

import java.util.Set;
import java.util.Map.Entry;

/**
 * A {@link Map} that maps Keys to set of values.
 *
 * @param  <X>  the type of Keys maintained by this map.
 * 
 * @param  <Y>  the type of mapped values.
 */
public interface MapToSet<X, Y> {
	
	/**
	 * Insert the given {@code value} in the set mapped to the specified {@code key}.
	 * <p>If the {@code key} is not already present a set containing the new {@code value} 
	 * will be created and associated to the specified {@code key}.
	 * 
	 * @param  key  {@code key} with which the specified {@code value} is to be associated.
	 * 
	 * @param value  {@code value} to be associated with the specified key.
	 * 
	 * @return {@code true} if and only if the {@code value} was successfully mapped.
	 */
	boolean put(X key, Y value);
	
	/**
	 * Removes the specified {@code value} from the set mapped to the specified {@code key} if it is present.
	 * <p>If the {@code value} is the last element of the set associated to the specified 
	 * {@code key} then removes the mapping for a {@code key} from this map.
	 * 
	 * @param  key  The {@code key} of the set where to search the {@code value} to remove.
	 * 
	 * @param  value  The {@code value} to remove from the set associated to the specified {@code key}.
	 * 
	 * @return  {@code true} if and only if the value was successfully removed.
	 */
	boolean remove(X key, Y value);
	
	/**
	 * Removes the mapping for a key from this map if is present.
	 * 
	 * @param  key  The {@code key} of the set to remove.
	 * 
	 * @return  The removed set.
	 */
	Set<Y> removeSet(X key);
	
	/**
	 * Get the set mapped to the given key if is present.
	 * 
	 * @param  key  The key of the set.
	 * 
	 * @return  The set associated to the given {@code key} if it is present , or
     *         {@code null} if there was no mapping for {@code key}.
	 */
	Set<Y> get(X key);

	/**
	 * Return {@code true} if this map contains a mapping for the specified
     * key, {@code false} otherwise.
	 * 
	 * @param  key  The key to check the mapping.
	 * 
	 * @return  {@code true} if this map contains a mapping for the specified
     *         key.
	 */
	boolean containsKey(X key);
	
	/**
	 * Check if the map is empty.
	 * 
	 * @return  {@code true} if this map contains no key-set mappings.
	 */
	boolean isEmpty();
	
	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * 
	 * @return  a set view of the mappings contained in this map
	 */
	Set<Entry<X, Set<Y>>> entrySet();

}
