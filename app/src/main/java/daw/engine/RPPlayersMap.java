package daw.engine;

import java.util.Set;

import daw.core.clip.RPClipPlayer;

import java.util.Map.Entry;

public interface RPPlayersMap {
	
	boolean putClipPlayer(Long step, RPClipPlayer clipPlayer);
	
	boolean removeClipPlayer(Long step, RPClipPlayer clipPlayer);
	
	Set<RPClipPlayer> removeClipPlayersAt(Long step);
	
	Set<RPClipPlayer> getClipPlayersAt(Long step);

	boolean containsStep(Long step);
	
	boolean isEmpty();
	
	Set<Entry<Long,Set<RPClipPlayer>>> entrySet();

}
