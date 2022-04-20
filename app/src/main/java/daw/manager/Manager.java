package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import javafx.util.Pair;
import planning.RPRole;
import planning.SpeechRole;

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
     */
    @Override
    public void addChannel(RPChannel.Type type, String title, Optional<String> description) {
        RPChannel channel;
        RPRole role;
        RPTapeChannel tapeChannel;
        channel = mixer.createChannel(type);
        role = this.createRole(type, title, description);
        }

    private RPRole createRole(RPChannel.Type type, String title, Optional<String> description) {
        if (type.equals(RPChannel.Type.BASIC)) {
            return null;
        } else if (type.equals(RPChannel.Type.GATED)) {
            return  null;
        } else if (type.equals(RPChannel.Type.RETURN)) {
            return null;
        }
        return null;
    }


    /**
     * This method creates a Sidechained Channel and all the corresponding components
     *
     * @param channel     the Channel to sidechain
     * @param title       the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     */
    @Override
    public void addSidechainedChannel(RPRole channel, String title, Optional<String> description) {

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
    public void createGroup(String groupName, RPChannel.Type type) {

    }

    /**
     * A method to create a sidechained group
     *
     * @param groupName the name of the group to be created
     * @param channel   the Channel to sidechain
     */
    @Override
    public void createSidechainedGroup(String groupName, RPRole channel) {

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
