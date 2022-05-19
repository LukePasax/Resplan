package daw.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import daw.core.channel.RPChannel;
import daw.core.clip.*;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import planning.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Manager implements RPManager {

    @JsonProperty
    private final RPMixer mixer;
    private final RPChannelLinker channelLinker;
    private final RPClipLinker clipLinker;
    @JsonProperty
    private final Map<RPRole, List<RPRole>> groupMap;
    private final RPClipConverter clipConverter;

    public Manager() {
        this.mixer = new Mixer();
        this.channelLinker = new ChannelLinker();
        this.clipLinker = new ClipLinker();
        this.groupMap = new HashMap<>();
        this.clipConverter = new ClipConverter();
        this.initializeGroups();
    }

    private void initializeGroups() {
        this.createGroup("Speech", RPRole.RoleType.SPEECH);
        this.createGroup("Effects", RPRole.RoleType.EFFECTS);
        this.createGroup("Soundtrack", RPRole.RoleType.SOUNDTRACK);
    }

    /**
     * This method creates a Channel of the given {@link daw.core.channel.RPChannel.Type} and all the corresponding
     * components.
     *
     * @param type the {@link planning.RPRole.RoleType} of {@link RPChannel} to be created
     * @param  title the Title to associate to the Channel
     * @param description the optional Description to associate to the Channel
     * @throws IllegalArgumentException if the given title is already in use
     */
    @Override
    public void addChannel(RPRole.RoleType type, String title, Optional<String> description) throws IllegalArgumentException {
        if (this.channelLinker.channelExists(title)) {
            throw new IllegalArgumentException("Channel already exists");
        } else if (title.equals("")) {
            throw new IllegalArgumentException("Title is mandatory");
        }
        final RPRole role = this.createRole(type, title, description);
        final RPTapeChannel tapeChannel = new TapeChannel();
        final RPChannel channel;
        if (type.equals(RPRole.RoleType.SPEECH)) {
            channel = this.mixer.createGatedChannel();
        } else {
            channel = this.mixer.createBasicChannel();
        }
        this.channelLinker.addChannelReferences(channel, tapeChannel, role);
        this.automaticGrouping(role);
    }

    /**
     * This method removes the Channel with the given title.
     *
     * @param title the title of the Channel to remove
     * @throws NoSuchElementException if a Channel with the given title does not exist
     */
    @Override
    public void removeChannel(String title) throws NoSuchElementException{
        if (!this.channelLinker.channelExists(title)) {
            throw new NoSuchElementException("The Channel does not exist");
        }
        this.getGroupList(this.getGroupName(this.channelLinker.getRole(title))).remove(this.channelLinker.getRole(title));
        this.channelLinker.removeChannel(this.channelLinker.getRole(title));
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
     * This method adds a Channel to a group.
     *
     * @param role the Channel to add to a group
     * @param groupName the name of the group
     * @throws NoSuchElementException if a group with the given name does not exist
     */
    @Override
    public void addToGroup(RPRole role, String groupName) throws NoSuchElementException {
        if(!this.groupContains(role, groupName)) {
            this.getGroupList(groupName).add(role);
            this.mixer.linkToGroup(this.channelLinker.getChannel(role),
                    this.channelLinker.getChannel(this.getGroup(groupName)));
        } else {
            throw new NoSuchElementException("Group does not exist");
        }
    }

    private boolean groupContains(RPRole role, String groupName) {
        if (this.groupExists(groupName)) {
            return this.getGroupList(groupName).contains(role);
        }
        throw new IllegalArgumentException();
    }

    /**
     * A method to create a group.
     *
     * @param groupName the name of the group to be created
     * @param type the type of group to be created
     * @throws IllegalArgumentException if a Group with the given name already exists
     */
    @Override
    public void createGroup(String groupName, RPRole.RoleType type) {
        if (this.groupExists(groupName)) {
            throw new IllegalArgumentException("Group already exists");
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
     * This method creates Clip and all the corresponding components.
     *
     * @param type the type of clip that needs to be created
     * @param title the title to associate with the clip
     * @param description the optional description to associate with the clip
     * @param channel the channel where to insert the clip
     * @param time the starting time of the clip in the channel
     * @param content the Optional content of the clip
     * @throws ImportException if there were problems importing the file
     */
    @Override
    public void addClip(RPPart.PartType type, String title, Optional<String> description,String channel,Double time,
                        Optional<File> content) throws ImportException, IllegalArgumentException {
        final EmptyClip clip = new EmptyClip();
        if (this.clipLinker.clipExists(title)) {
            throw new IllegalArgumentException("Clip already exists");
        } else if (title.equals("")) {
            throw new IllegalArgumentException("Title is mandatory");
        }
        if (content.isPresent()) {
            try {
                this.clipConverter.fromEmptyToSampleClip(clip, content.get());
            } catch (FileFormatException | OperationUnsupportedException | IOException exception) {
                throw new ImportException("Error in loading file");
            }
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
        channelLinker.getTapeChannel(channelLinker.getRole(title)).insertRPClip(clip, time);
    }

    /**
     * This method adds a content to a Clip.
     *
     * @param title   the title of the Clip
     * @param content the content to put into the Clip
     * @throws NoSuchElementException if no Clip with the given title exists
     */
    @Override
    public void addFileToClip(String title, File content) throws ImportException , NoSuchElementException {
        if (this.clipLinker.clipExists(title)) {
            throw new NoSuchElementException("The Clip does not exist");
        }
        this.removeFileFromClip(title);
        try {
            this.clipConverter.fromEmptyToSampleClip((EmptyClip) this.clipLinker.getClip(this.clipLinker.getPart(title)), content);
        } catch (OperationUnsupportedException | FileFormatException | IOException exception) {
            throw new ImportException("Error in loading file");
        }
    }

    /**
     * This method removes the content from a Clip.
     *
     * @param title the title of the Clip
     * @throws IllegalArgumentException if the Clip has no content
     * @throws NoSuchElementException   if no Clip with the given title exists
     */
    @Override
    public void removeFileFromClip(String title) throws NoSuchElementException, IllegalArgumentException {
        if (!this.clipLinker.clipExists(title)) {
            throw new NoSuchElementException("The Clip does not exist");
        }
        if (!this.clipLinker.getClip(this.clipLinker.getPart(title)).getClass().equals(SampleClip.class)) {
            throw new IllegalArgumentException("The Clip has no content");
        }
        this.clipConverter.fromSampleToEmptyClip((SampleClip) this.clipLinker.getClip(this.clipLinker.getPart(title)));
    }

    /**
     * This method removes the Clip with the given title.
     *
     * @param channel the Channel with the Clip to be removed
     * @param clip    the tile of the Clip to be removed
     * @param time    the time of the Clip in the Channel
     * @throws NoSuchElementException if the Clip does not exist
     */
    @Override
    public void removeClip(String channel, String clip, double time) throws ClipNotFoundException {
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel)).removeClip(time);
        this.clipLinker.removeClip(this.getClipLinker().getPart(clip));
    }

    /**
     * A method to return the {@link RPRole} associated with a groupName.
     *
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     * @throws NoSuchElementException if the group does non exist
     */
    @Override
    public RPRole getGroup(String groupName) throws NoSuchElementException {
        return this.groupMap.keySet().stream().filter(k -> k.getTitle().equals(groupName)).findAny().orElseThrow(() ->
                new NoSuchElementException("Group does not exist"));
    }

    /**
     * A method to return the list of Channel associated to a group.
     *
     * @param groupName the name of the group
     * @return a List of {@link RPRole}
     * @throws NoSuchElementException if the group does not exist
     */
    @Override
    public List<RPRole> getGroupList(String groupName) throws NoSuchElementException {
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
