package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import planning.RPRole;

/**
 * This interface represents a class to link a {@link RPRole} to its corresponding Pair of {@link RPChannel} and
 * {@link RPTapeChannel}
 */

public interface RPChannelLinker {

	/**
	 * This method links all the given components
	 * @param channel the low-level {@link RPChannel} to link
	 * @param tapeChannel the {@link RPTapeChannel} to link
	 * @param role the high-level channel, represented by a {@link RPRole} to link
	 */
	void addChannelReferences(RPChannel channel, RPTapeChannel tapeChannel, RPRole role);

	/**
	 * A method that returns the {@link RPChannel} linked to the given {@link RPRole}
	 * @param role the {@link RPRole} linked to the wanted {@link  RPChannel}
	 * @return the {@link RPChannel} linked to the given {@link RPRole}
	 */
	RPChannel getChannel(RPRole role);

	/**
	 * A method that returns the {@link RPTapeChannel} linked to the given {@link RPRole}
	 * @param role the {@link RPRole} linked to the wanted {@link  RPTapeChannel}
	 * @return the {@link RPTapeChannel} linked to the given {@link RPRole}
	 */
	RPTapeChannel getTapeChannel(RPRole role);

}
