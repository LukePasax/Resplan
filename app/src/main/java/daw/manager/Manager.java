package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import javafx.util.Pair;
import net.beadsproject.beads.ugens.Gain;
import planning.RPRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Manager implements RPManager{

    private final RPMixer mixer;
    private final RPChannelLinker channelLinker;
    private final RPClipLinker clipLinker;
    private final Map<String, Pair<RPRole, List<RPRole>>> groupList;

    Manager() {
        mixer = new Mixer();
        channelLinker = new ChannelLinker();
        clipLinker = new ClipLinker();
        groupList = new HashMap<>();
    }

    /**
     * This method creates a Channel of the given {@link RPChannel.Type} and all the corresponding
     * components
     *
     * @param type the {@link RPChannel.Type} of {@link RPChannel} to be created
     * @param gain the optional {@link Gain} for a sidechained {@link RPChannel}
     */
    @Override
    public void addChannel(RPChannel.Type type, Optional<Gain> gain) {
        RPChannel channel;
        RPRole role;
        RPTapeChannel tapeChannel;
    }

    /**
     * This method adds a {@link RPChannel} to a group
     *
     * @param channel   the {@link RPChannel} to add to a group
     * @param groupName the name of the group
     */
    @Override
    public void addToGroup(RPChannel channel, String groupName) {

    }

    /**
     * A method to create a group
     *
     * @param groupName the name of the group to be created
     */
    @Override
    public void createGroup(String groupName) {

    }

    /**
     * This method creates a Clip and all the corresponding components
     */
    @Override
    public void addClip() {

    }

    /**
     * A method to return the {@link RPRole} associated with a groupName
     *
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     */
    @Override
    public RPRole getGroup(String groupName) {
        return null;
    }
}
