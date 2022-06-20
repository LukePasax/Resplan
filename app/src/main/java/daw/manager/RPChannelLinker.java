package daw.manager;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import daw.utilities.RPPair;
import planning.RPRole;
import java.util.List;
import java.util.Set;

/**
 * This interface represents a class to link a {@link RPRole} to its corresponding Pair of {@link RPChannel} and
 * {@link RPTapeChannel}.
 */
@JsonDeserialize(as = ChannelLinker.class)
public interface RPChannelLinker {

	/**
	 * This method links all the given components.
	 *
	 * @param channel the low-level {@link RPChannel} to link
	 * @param tapeChannel the {@link RPTapeChannel} to link
	 * @param role the high-level channel, represented by a {@link RPRole} to link
	 */
	void addChannelReferences(RPChannel channel, RPTapeChannel tapeChannel, RPRole role);

	/**
	 * A method that returns the {@link RPChannel} linked to the given {@link RPRole}.
	 *
	 * @param role the {@link RPRole} linked to the wanted {@link  RPChannel}
	 * @return the {@link RPChannel} linked to the given {@link RPRole}
	 */
	RPChannel getChannel(RPRole role);

	/**
	 * A method that returns the {@link RPTapeChannel} linked to the given {@link RPRole}.
	 *
	 * @param role the {@link RPRole} linked to the wanted {@link  RPTapeChannel}
	 * @return the {@link RPTapeChannel} linked to the given {@link RPRole}
	 */
	RPTapeChannel getTapeChannel(RPRole role);

	/**
	 * A method that returns the {@link  RPRole} with the given title.
	 *
	 * @param title the title of the part
	 * @return the {@link RPRole} with the given title
	 */
	RPRole getRole(String title);

	/**
	 * A method to remove a Channel from the list with all its corresponding components.
	 *
	 * @param role the {@link RPRole} of the Channel that needs to be eliminated
	 */
	void removeChannel(RPRole role);

	/**
	 * This method returns all the {@link RPRole} of the type given present in the map.
	 *
	 * @param type the type to search
	 * @return a set containing all the {@link  RPRole} of the type given
	 */
	Set<RPRole> getRoleSet(RPRole.RoleType type);

	/**
	 * This method returns a Set of Pairs of all the low-level parts of a Channel.
	 *
	 * @return a set of pairs of {@link RPChannel} and {@link RPTapeChannel}
	 */
	Set<RPPair<RPChannel, RPTapeChannel>> getAudioSet();

	/**
	 * This method is used to check if a Channel with the given title exists.
	 *
	 * @param title the title of the Channel that need to be checked
	 * @return true if the Channel exists, false otherwise
	 */
	boolean channelExists(String title);

    List<RPRole> getRolesAndGroups();
}
