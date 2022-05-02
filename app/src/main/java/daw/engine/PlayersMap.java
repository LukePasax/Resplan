package daw.engine;

import java.util.Set;

import daw.core.clip.RPClipPlayer;

import java.util.Map.Entry;

public interface PlayersMap {
	
	boolean putClipPlayer(Long step, RPClipPlayer clip);
	
	boolean removeClipPlayer(Long step, RPClipPlayer clip);
	
	Set<RPClipPlayer> removeClipPlayersAt(Long step);
	
	Set<RPClipPlayer> getClipPlayersAt(Long step);

	boolean containsStep(Long step);
	
	boolean isEmpty();
	
	Set<Entry<Long,Set<RPClipPlayer>>> entrySet();

}
