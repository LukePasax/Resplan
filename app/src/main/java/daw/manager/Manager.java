package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.*;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import planning.*;

import java.io.File;
import java.util.*;

public class Manager implements RPManager{

    private final RPMixer mixer;
    private final RPChannelLinker channelLinker;
    private final RPClipLinker clipLinker;
    private final Map<RPRole, List<RPRole>> groupMap;

    public Manager() {
        this.mixer = new Mixer();
        this.channelLinker = new ChannelLinker();
        this.clipLinker = new ClipLinker();
        this.groupMap = new HashMap<>();
        this.initializeGroups();
    }

    private void initializeGroups() {
        this.createGroup("Speech", RPRole.RoleType.SPEECH);
        this.createGroup("Effects", RPRole.RoleType.EFFECTS);
        this.createGroup("Soundtrack", RPRole.RoleType.SOUNDTRACK);

    }

    /**
     * This method creates a Channel of the given {@link RPChannel.Type} and all the corresponding
     * components
     *
     * @param type the {@link RPChannel.Type} of {@link RPChannel} to be created
     */
    @Override
    public void addChannel(RPRole.RoleType type, String title, Optional<String> description) {
        if (this.channelLinker.getRoleSet(type).stream().anyMatch(r -> r.getTitle().equals(title))) {
            throw new IllegalArgumentException();
        }
        final RPRole role = this.createRole(type, title, description);
        final RPTapeChannel tapeChannel = new TapeChannel();
        final RPChannel channel;
        if (type.equals(RPRole.RoleType.SPEECH)) {
            channel = this.mixer.createGatedChannel();
        } else {
            channel = this.mixer.createBasicChannel();
        }
        this.channelLinker.addChannelReferences(channel,tapeChannel,role);
        this.automaticGrouping(role);
    }

    /**
     * This method removes the Channel with the given title
     *
     * @param title the title of the Channel to remove
     */
    @Override
    public void removeChannel(String title) {
        this.getGroupList(this.getGroupName(this.channelLinker.getPart(title))).remove(this.channelLinker.getPart(title));
        this.channelLinker.removeChannel(this.channelLinker.getPart(title));
    }

    private String getGroupName(RPRole part) {
        return this.groupMap.entrySet().stream().filter(e -> e.getValue().contains(part)).map(e -> e.getKey().getTitle()).findAny().orElseThrow();
    }

    private void automaticGrouping(RPRole role) {
        if (role.getType().equals(RPRole.RoleType.SPEECH)) {
            this.addToGroup(role, "Speech");
        } else if (role.getType().equals(RPRole.RoleType.EFFECTS)) {
            this.addToGroup(role, "Effects");
        } else {
            this.addToGroup(role, "Soundtrack");
        }
    }

    private boolean groupExists(String name) {
        return this.groupMap.keySet().stream().anyMatch(k -> k.getTitle().equals(name));
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
     * This method adds a Channel to a group
     *
     * @param role the Channel to add to a group
     * @param groupName the name of the group
     */
    @Override
    public void addToGroup(RPRole role, String groupName) {
        if(!this.groupContains(role, groupName)) {
            this.getGroupList(groupName).add(role);
            this.mixer.linkToGroup(this.channelLinker.getChannel(role),
                    this.channelLinker.getChannel(this.getGroup(groupName)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean groupContains(RPRole role, String groupName) {
        if (this.groupExists(groupName)) {
            return this.getGroupList(groupName).contains(role);
        }
        throw new IllegalArgumentException();
    }

    //TODO
    /**
     * A method to create a group
     *
     * @param groupName the name of the group to be created
     * @param type the type of group to be created
     */
    @Override
    public void createGroup(String groupName, RPRole.RoleType type) {
        if (this.groupExists(groupName)) {
            throw new IllegalArgumentException();
        } else {
            final RPChannel channel;
            if (type.equals(RPRole.RoleType.SOUNDTRACK)) {
                channel = this.mixer.createSidechained(this.channelLinker.getChannel(this.groupMap.keySet().stream()
                        .filter(k -> k.getTitle().equals("Speech")).findAny().orElseThrow()));
            } else {
                channel = this.mixer.createBasicChannel();
            }
            final RPRole role = this.createRole(type, groupName, Optional.empty());
            this.channelLinker.addChannelReferences(channel, new TapeChannel(), role);
            this.groupMap.put(role, new ArrayList<>());
        }
    }

    /**
     * This method creates a Clip and all the corresponding components
     */
    @Override
    public void addClip(RPPart.PartType type, String title, Optional<String> description, Optional<File> content) {
        final RPClip clip;
        if (content.isPresent()) {
            clip = new FileClip(content.get());
        } else {
            clip = new EmptyClip();
        }
        final RPPart part;
        if (type.equals(RPPart.PartType.SPEECH)) {
            part = description.map(s -> new SpeechPart(title, s)).orElseGet(() -> new SpeechPart(title));
        } else if (type.equals(RPPart.PartType.EFFECTS)) {
            part = description.map(s -> new EffectsPart(title, s)).orElseGet(() -> new EffectsPart(title));
        } else {
            part = description.map(s -> new SoundtrackPart(title, s)).orElseGet(() -> new SoundtrackPart(title));
        }
        this.clipLinker.addClipReferences(clip, part);
    }

    /**
     * A method to return the {@link RPRole} associated with a groupName
     *
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     */
    @Override
    public RPRole getGroup(String groupName) {
        return this.groupMap.keySet().stream().filter(k -> k.getTitle().equals(groupName)).findAny().orElseThrow();
    }

    /**
     * A method to return the list of Channel associated to a group
     *
     * @param groupName the name of the group
     * @return a List of {@link RPRole}
     */
    @Override
    public List<RPRole> getGroupList(String groupName) {
        return this.groupMap.get(this.getGroup(groupName));
    }

    /**
     * @return the {@link ChannelLinker} of this Manager
     */
    @Override
    public RPChannelLinker getChannelLinker() {
        return this.channelLinker;
    }

    /**
     * @return the {@link RPClipLinker} of this manager
     */
    @Override
    public RPClipLinker getClipLinker() {
        return this.clipLinker;
    }
}
