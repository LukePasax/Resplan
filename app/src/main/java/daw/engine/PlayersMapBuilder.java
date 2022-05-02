package daw.engine;

import java.util.Optional;

import daw.manager.ChannelLinker;

public interface PlayersMapBuilder {
	
	RPPlayersMap build();
	
	PlayersMapBuilder setChannelLinker(ChannelLinker channelLinker);
	
	PlayersMapBuilder addSampleClipsBetween(Optional<Double> timeIn, Optional<Double> timeOut);

}
