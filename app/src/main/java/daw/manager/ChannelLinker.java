package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import javafx.util.Pair;
import planning.RPRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class ChannelLinker implements RPChannelLinker {

    private static Map<RPRole, Pair<RPChannel, RPTapeChannel>> channelMap;

    ChannelLinker() {
        channelMap = new HashMap<>();
    }

    /**
     * This method links all the given components
     * @param channel the low-level {@link RPChannel} to link
     * @param tapeChannel the {@link RPTapeChannel} to link
     * @param role the high-level channel, represented by a {@link RPRole} to link
     */
    @Override
    public void addChannelReferences(RPChannel channel, RPTapeChannel tapeChannel, RPRole role) {
        channelMap.put(role, new Pair<>(channel,tapeChannel));
    }

    /**
     * A method that returns the {@link RPChannel} linked to the given {@link RPRole}
     * @param role the {@link RPRole} linked to the wanted {@link  RPChannel}
     * @return the {@link RPChannel} linked to the given {@link RPRole}
     */
    @Override
    public RPChannel getChannel(RPRole role) {
        return channelMap.get(role).getKey();
    }

    /**
     * A method that returns the {@link RPTapeChannel} linked to the given {@link RPRole}
     * @param role the {@link RPRole} linked to the wanted {@link  RPTapeChannel}
     * @return the {@link RPTapeChannel} linked to the given {@link RPRole}
     */
    @Override
    public RPTapeChannel getTapeChannel(RPRole role) {
        return channelMap.get(role).getValue();
    }

    /**
     * This method returns all the {@link RPRole} of the type given present in the map
     *
     * @param type the type to search
     * @return a set containing all the {@link  RPRole} of the type given
     */
    @Override
    public Set<RPRole> getRoleSet(RPRole.RoleType type) {
        return channelMap.keySet().stream().filter(k -> k.getType().equals(type)).collect(Collectors.toSet());
    }
}
