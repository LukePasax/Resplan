package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import planning.RPRole;

public interface RPChannelLinker {
	
	void addChannelReferences(RPChannel channel, RPTapeChannel tapeChannel, RPRole role);
	
	RPChannel getChannel(RPRole role);
	
	RPTapeChannel getTapeChannel(RPRole role);

}
