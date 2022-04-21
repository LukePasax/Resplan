package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPClip;
import daw.core.clip.RPTapeChannel;
import daw.core.clip.SampleClip;
import daw.core.clip.TapeChannel;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import javafx.util.Pair;
import planning.*;

import java.util.*;
import java.util.stream.Collectors;

public class Manager implements RPManager{

    private final RPMixer mixer;
    private final RPChannelLinker channelLinker;
    private final RPClipLinker clipLinker;
    private final Map<String, Pair<RPRole, List<RPRole>>> groupList;

    public Manager() {
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

    private void automaticGrouping(RPRole role) {
        String name;
        if (role.getType().equals(RPRole.RoleType.SPEECH)) {
            name = "Speech";
        } else {
            name = "Fx";
        }
        if (!groupExists(name)) {
            createGroup(name, role.getType());
        }
        addToGroup(role, name);
    }

    private void automaticSidechainedGrouping(RPRole role, RPRole channel) {
        String name = "Soundtrack";
        if (!groupExists(name)) {
            this.createGroup(name, RPRole.RoleType.SOUNDTRACK);
        }
        addToGroup(role, name);
        mixer.linkToSidechainedGroup(channelLinker.getChannel(role), channelLinker.getChannel(getGroup(name)),
                channelLinker.getChannel(channel));
    }


    private boolean verifyPresence(RPRole.RoleType type) {
        return channelLinker.getRoleSet(type).stream().filter(e -> !groupList.values().stream().map(Pair::getKey).
                collect(Collectors.toSet()).contains(e)).anyMatch(e -> e.getType().equals(type));
    }

    private boolean groupExists(String name) {
        return groupList.containsKey(name);
    }

    private boolean verifyForSidechain() {
        return !channelLinker.getRoleSet(RPRole.RoleType.SPEECH).isEmpty() && groupList.values().stream().map(Pair::getKey)
                .anyMatch(k -> k.getType().equals(RPRole.RoleType.SPEECH));
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
        RPChannel sChannel;
        if (!this.verifyForSidechain()) {
            sChannel = mixer.createChannel(RPChannel.Type.BASIC);
        } else {
            if (!groupExists("Speech")) {
                sChannel = mixer.createSidechained(channelLinker.getChannel(channelLinker.getRoleSet(RPRole.RoleType.SPEECH)
                        .stream().findAny().get()));
            } else {
                sChannel = mixer.createSidechained(channelLinker.getChannel(this.getGroup("Speech")));
            }
        }
        channelLinker.addChannelReferences(sChannel,tapeChannel,role);
        this.automaticSidechainedGrouping(role, channel);
    }

    /**
     * This method adds a Channel to a group
     *
     * @param role the Channel to add to a group
     * @param groupName the name of the group
     */
    @Override
    public void addToGroup(RPRole role, String groupName) {
        if(!groupContains(role, groupName)) {
            getGroupList(groupName).add(role);
            mixer.linkToGroup(channelLinker.getChannel(role), channelLinker.getChannel(getGroup(groupName)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean groupContains(RPRole role, String groupName) {
        if (this.groupExists(groupName)) {
            return getGroupList(groupName).contains(role);
        }
        throw new IllegalArgumentException();
    }

    /**
     * This method removes a Channel from a group
     *
     * @param role      the Channel to remove from a group
     * @param groupName the name of the Group
     */
    @Override
    public void removeFromGroup(RPRole role, String groupName) {
        if (groupContains(role, groupName)) {
            getGroupList(groupName).remove(role);
            mixer.unlinkFromGroup(channelLinker.getChannel(role), channelLinker.getChannel(getGroup(groupName)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This method moves a Channel from a group to another
     *
     * @param role     the Channel to move
     * @param oldGroup the current group of the Channel
     * @param newGroup the group where the Channel is going to be moved
     */
    @Override
    public void switchGroup(RPRole role, String oldGroup, String newGroup) {
        this.removeFromGroup(role, oldGroup);
        this.addToGroup(role, newGroup);
    }

    /**
     * A method to create a group
     *
     * @param groupName the name of the group to be created
     * @param type the type of group to be created
     */
    @Override
    public void createGroup(String groupName, RPRole.RoleType type) {
        if (groupExists(groupName)) {
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
    public void createSidechainedGroup(String groupName, RPRole channel) {
        if (groupExists(groupName)) {
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
    public void addClip(RPPart.PartType type) {
        RPClip clip;
        RPPart part;
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

    /**
     * A method to return the list of Channel associated to a group
     *
     * @param groupName the name of the group
     * @return a List of {@link RPRole}
     */
    @Override
    public List<RPRole> getGroupList(String groupName) {
        return groupList.get(groupName).getValue();
    }
}
