package daw.engine;

import java.util.Optional;

import daw.manager.ChannelLinker;

/**
 * Build an {@link RPPlayersMap} step by step.
 */
public interface PlayersMapBuilder {
	
	/**
	 * Returns the configured map.
	 * 
	 * @return  The configured map
	 */
	RPPlayersMap build();
	
	/**
	 * Set the channel linker from which get clips.
	 * 
	 * @param channelLinker  The channel linker.
	 * 
	 * @return  This builder
	 */
	PlayersMapBuilder setChannelLinker(ChannelLinker channelLinker);
	
	/**
	 * Get the sample clips between the given times and creates a {@link RPClipPlayer} for each.
	 * Then add all the created players to the map.
	 * 
	 * @param timeIn  The time from which to select clip
	 * 
	 * @param timeOut  The time up to which clips are selected
	 * 
	 * @return  This builder
	 */
	PlayersMapBuilder addSampleClipsBetween(Optional<Double> timeIn, Optional<Double> timeOut);

}
