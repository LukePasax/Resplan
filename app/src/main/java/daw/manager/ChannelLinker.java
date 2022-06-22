package daw.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import daw.utilities.RPPair;
import planning.RPRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ChannelLinker implements RPChannelLinker {

    @JsonProperty
    private final Map<RPRole, RPPair<RPChannel, RPTapeChannel>> channelMap;

    ChannelLinker() {
        this.channelMap = new HashMap<>();
    }

    /**
     * This method links all the given components.
     *
     * @param channel the low-level {@link RPChannel} to link
     * @param tapeChannel the {@link RPTapeChannel} to link
     * @param role the high-level channel, represented by a {@link RPRole} to link
     */
    @Override
    public void addChannelReferences(final RPChannel channel, final RPTapeChannel tapeChannel, final RPRole role) {
        channelMap.put(role, new RPPair<>(channel, tapeChannel));
    }

    /**
     * A method that returns the {@link RPChannel} linked to the given {@link RPRole}.
     *
     * @param role the {@link RPRole} linked to the wanted {@link  RPChannel}
     * @return the {@link RPChannel} linked to the given {@link RPRole}
     */
    @Override
    public RPChannel getChannel(final RPRole role) {
        return channelMap.get(role).getKey();
    }

    /**
     * A method that returns the {@link RPTapeChannel} linked to the given {@link RPRole}.
     *
     * @param role the {@link RPRole} linked to the wanted {@link  RPTapeChannel}
     * @return the {@link RPTapeChannel} linked to the given {@link RPRole}
     */
    @Override
    public RPTapeChannel getTapeChannel(final RPRole role) {
        return channelMap.get(role).getValue();
    }

    /**
     * A method that returns the {@link  RPRole} with the given title.
     *
     * @param title the title of the {@link RPRole}
     * @return the {@link RPRole} with the given title
     */
    @Override
    public RPRole getRole(final String title) {
        return channelMap.keySet().stream().filter(k -> k.getTitle().equals(title)).findAny().orElseThrow();
    }

    /**
     * A method to remove a Channel from the list with all its corresponding components.
     *
     * @param role the {@link RPRole} of the Channel that needs to be eliminated
     */
    @Override
    public void removeChannel(final RPRole role) {
        channelMap.remove(role);
    }

    /**
     * This method returns all the {@link RPRole} of the type given present in the map.
     *
     * @param type the type to search
     * @return a set containing all the {@link  RPRole} of the type given
     */
    @Override
    public Set<RPRole> getRoleSet(final RPRole.RoleType type) {
        return channelMap.keySet().stream().filter(k -> k.getType().equals(type)).collect(Collectors.toSet());
    }

    /**
     * This method returns a Set of Pairs of all the low-level parts of a Channel.
     *
     * @return a set of pairs of {@link RPChannel} and {@link RPTapeChannel}
     */
    @Override
    @JsonIgnore
    public Set<RPPair<RPChannel, RPTapeChannel>> getAudioSet() {
        return new HashSet<>(channelMap.values());
    }

    /**
     * This method is used to check if a Channel with the given title exists.
     *
     * @param title the title of the Channel that need to be checked
     * @return true if the Channel exists, false otherwise
     */
    @Override
    public boolean channelExists(final String title) {
        return this.channelMap.keySet().stream().anyMatch(k -> k.getTitle().equals(title));
    }

    @Override
    @JsonIgnore
    public List<RPRole> getRolesAndGroups() {
        return new ArrayList<>(this.channelMap.keySet());
    }
}
