package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import daw.core.clip.TapeChannel;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import javafx.util.Pair;
import planning.EffectsRole;
import planning.RPRole;
import planning.SoundtrackRole;
import planning.SpeechRole;

import java.util.*;

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
    public void addChannel(RPRole.RoleType type, String title, Optional<String> description) {
        RPRole role = this.createRole(type, title, description);
        RPTapeChannel tapeChannel = new TapeChannel();
        RPChannel channel;
        if (type.equals(RPRole.RoleType.SPEECH)) {
            channel = mixer.createChannel(RPChannel.Type.GATED);
        } else {
            channel = mixer.createChannel(RPChannel.Type.BASIC);
        }
        channelLinker.addChannelReferences(channel,tapeChannel,role);
        this.automaticGrouping(role);
    }

    //TODO
    private void automaticGrouping(RPRole role) {
        String name;
        if (role.getType().equals(RPRole.RoleType.SPEECH)) {
            name = "Speech";
        } else {

        }
        if (this.verifyPresence(role.getType())) {

        }
    }

    private boolean verifyGroup(String name) {
        return groupList.keySet().stream().anyMatch(k -> k.equals(name));
    }

    private boolean verifyPresence(RPRole.RoleType type) {
        return !channelLinker.getRoleSet(type).isEmpty() && groupList.entrySet().stream().map(e -> e.getValue().getKey())
                .anyMatch(k -> k.getType().equals(type));
    }

    private RPRole createRole(RPRole.RoleType type, String title, Optional<String> description) {
        if (type.equals(RPRole.RoleType.SPEECH)) {
            return description.map(s -> new SpeechRole(title, s)).orElseGet(() -> new SpeechRole(title));
        } else if (type.equals(RPRole.RoleType.EFFECTS)) {
            return description.map(s -> new EffectsRole(title, s)).orElseGet(() -> new EffectsRole(title));
        } else if (type.equals(RPRole.RoleType.SOUNDTRACK)) {
            return description.map(s -> new SoundtrackRole(title, s)).orElseGet(() -> new SoundtrackRole(title));
        } else {
            return null;
        }
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
        RPRole role = this.createRole(RPRole.RoleType.SOUNDTRACK, title, description);
        RPTapeChannel tapeChannel = new TapeChannel();
        RPChannel Schannel;
        if (!this.verifyPresence(RPRole.RoleType.SPEECH)) {
            Schannel = mixer.createChannel(RPChannel.Type.BASIC);
        } else {
            if (!verifyGroup("Speech")) {
                Schannel = mixer.createSidechained(channelLinker.getChannel(channelLinker.getRoleSet(RPRole.RoleType.SPEECH)
                        .stream().findAny().get()));
            } else {
                Schannel = mixer.createSidechained(channelLinker.getChannel(groupList.get("Speech").getKey()));
            }
        }
    }

    //TODO
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
     * @param type
     */
    @Override
    public void createGroup(String groupName, RPRole.RoleType type) throws IllegalArgumentException {
        if (groupList.containsKey(groupName)) {
            throw new IllegalArgumentException();
        } else {
            RPRole role = this.createRole(type, groupName, Optional.empty());
            channelLinker.addChannelReferences(mixer.createChannel(RPChannel.Type.BASIC), new TapeChannel(),role);
            groupList.put(groupName, new Pair<>(role,new ArrayList<>()));
        }
    }

    /**
     * A method to create a sidechained group
     *
     * @param groupName the name of the group to be created
     * @param channel   the Channel to sidechain
     */
    @Override
    public void createSidechainedGroup(String groupName, RPRole channel) throws IllegalArgumentException {
        if (groupList.containsKey(groupName)) {
            throw new IllegalArgumentException();
        } else {
            RPRole role = this.createRole(RPRole.RoleType.SOUNDTRACK,groupName,Optional.empty());
            channelLinker.addChannelReferences(mixer.createSidechained(channelLinker.getChannel(channel)),
                    new TapeChannel(), role);
            groupList.put(groupName,new Pair<>(role,new ArrayList<>()));
        }
    }

    //TODO
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
        return groupList.get(groupName).getKey();
    }
}
