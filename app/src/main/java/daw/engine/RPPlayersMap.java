package daw.engine;

import java.util.Set;

import daw.core.clip.RPClipPlayer;

import java.util.Map.Entry;

/**
 * An object that maps steps (Keys) to sets of {@link RPClipPlayer}.
 * An RPPlayersMap cannot contain duplicate steps; for each step are collected 
 * multiple players.
 */
public interface RPPlayersMap {
	
	/**
	 * Insert a clip player in the set associated to the specified step.
	 * If the step is not present it will be added.
	 * 
	 * @param  step  The step with which the clip player is to be associated.
	 * 
	 * @param  clipPlayer  The clip player to associate at the given step.
	 * 
	 * @return  {@code true} If and only if the player was succesfully 
	 * 							added to the step set.
	 * 			{@code false} otherwise.
	 */
	boolean putClipPlayer(Long step, RPClipPlayer clipPlayer);
	
	/**
	 * Remove a clip player from the set associated to the specified step.
	 * If the removed clip player was the last player associated to the specified step, 
	 * the corrisponding set will be removed.
	 * 
	 * @param  step  The step whose clip player is to be removed.
	 * 
	 * @param  clipPlayer  The clip player to remove.
	 * 
	 * @return  {@code true} If and only if the player was succesfully 
	 * 							removed to the step set.
	 * 			{@code false} otherwise.
	 */
	boolean removeClipPlayer(Long step, RPClipPlayer clipPlayer);
	
	/**
	 * Remove the set with all the clip players associated to the specified step from the map .
	 * 
	 * @param  step  The step to remove from the map.
	 * 
	 * @return  The removed set or {@code null} if the step is not contained in the map.
	 */
	Set<RPClipPlayer> removeClipPlayersAt(Long step);
	
	/**
	 * Get the set of clip players associated to the specified step.
	 * 
	 * @param  step  The step to associated to the set.
	 * 
	 * @return  The set associated to the step.
	 */
	Set<RPClipPlayer> getClipPlayersAt(Long step);

	/**
	 * Check if the map contains the specified step.
	 * 
	 * @param  step  The step whose presence in this map is to be tested.
	 * 
	 * @return  {@code true} if this map contains a mapping for the specified
     *         step
	 */
	boolean containsStep(Long step);
	
	/**
	 * Returns {@code true} if this map contains no step-value mappings.
	 * 
	 * @return {@code true} if this map contains no step-value mappings
	 */
	boolean isEmpty();
	
	/**
	 * Returns a {@link Set} view of the mappings contained in this map.
	 * 
	 * @return a set view of the mappings contained in this map.
	 */
	Set<Entry<Long, Set<RPClipPlayer>>> entrySet();

}
