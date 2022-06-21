package daw.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.core.channel.RPChannel;
import daw.core.clip.ClipConverter;
import daw.core.clip.ClipNotFoundException;
import daw.core.clip.EmptyClip;
import daw.core.clip.RPClip;
import daw.core.clip.RPClipConverter;
import daw.core.clip.RPTapeChannel;
import daw.core.clip.SampleClip;
import daw.core.clip.TapeChannel;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import planning.EffectsPart;
import planning.EffectsRole;
import planning.RPPart;
import planning.RPRole;
import planning.RPSection;
import planning.RPTimeline;
import planning.SectionImpl;
import planning.SimpleSpeaker;
import planning.SimpleSpeakerRubric;
import planning.SoundtrackPart;
import planning.SoundtrackRole;
import planning.Speaker;
import planning.SpeakerRubric;
import planning.SpeechPart;
import planning.SpeechRole;
import planning.TimelineImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Manager implements RPManager {

    private static final double MIN_LENGTH = 600_000;
    private static final double MIN_SPACING = 120_000;

    private final RPMixer mixer;
    private final RPChannelLinker channelLinker;
    private final RPClipLinker clipLinker;
    @JsonProperty
    private final Map<RPRole, List<RPRole>> groupMap;
    @JsonProperty
    private final RPTimeline timeline;
    private final RPClipConverter clipConverter;
    private final SpeakerRubric rubric;
    private double projectLength;

    public Manager() {
        this.mixer = new Mixer();
        this.channelLinker = new ChannelLinker();
        this.clipLinker = new ClipLinker();
        this.groupMap = new HashMap<>();
        this.clipConverter = new ClipConverter();
        this.projectLength = MIN_LENGTH;
        this.timeline = new TimelineImpl();
        this.rubric = new SimpleSpeakerRubric();
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
    public void addChannel(
            final RPRole.RoleType type, final String title, final Optional<String> description)
            throws IllegalArgumentException {
        if (this.channelLinker.channelExists(title)) {
            throw new IllegalArgumentException("Channel already exists");
        } else if ("".equals(title)) {
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
    public void removeChannel( final String title) throws NoSuchElementException {
        if (!this.channelLinker.channelExists(title)) {
            throw new NoSuchElementException("The Channel does not exist");
        }
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(title))
                .getClipWithTimeIterator().forEachRemaining(e ->
                        this.clipLinker.removeClip(this.getClipLinker().getPartFromClip(e.getValue())));
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(title)).clearTape();
        this.getGroupList(this.getGroupName(this.channelLinker.getRole(title)))
                .remove(this.channelLinker.getRole(title));
        this.channelLinker.removeChannel(this.channelLinker.getRole(title));
    }

    private String getGroupName( final RPRole part) {
        return this.groupMap.entrySet().stream()
                .filter(e -> e.getValue().contains(part))
                .map(e -> e.getKey().getTitle())
                .findAny()
                .orElseThrow();
    }

    private void automaticGrouping( final RPRole role) {
        if (role.getType().equals(RPRole.RoleType.SPEECH)) {
            this.addToGroup(role, "Speech");
        } else if (role.getType().equals(RPRole.RoleType.EFFECTS)) {
            this.addToGroup(role, "Effects");
        } else {
            this.addToGroup(role, "Soundtrack");
        }
    }

    private boolean groupExists( final String name) {
        return this.groupMap.keySet().stream().anyMatch(k -> k.getTitle().equals(name));
    }

    private RPRole createRole( final RPRole.RoleType type, final String title, final Optional<String> description) {
        if (type.equals(RPRole.RoleType.SPEECH)) {
            return description.map(s -> new SpeechRole(title, s)).orElseGet(() -> new SpeechRole(title));
        } else if (type.equals(RPRole.RoleType.EFFECTS)) {
            return description.map(s -> new EffectsRole(title, s)).orElseGet(() -> new EffectsRole(title));
        } else {
            return description.map(s -> new SoundtrackRole(title, s)).orElseGet(() -> new SoundtrackRole(title));
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
    public void addToGroup( final RPRole role, final String groupName) throws NoSuchElementException {
        if(!this.groupContains(role, groupName)) {
            this.getGroupList(groupName).add(role);
            this.mixer.linkToGroup(this.channelLinker.getChannel(role),
                    this.channelLinker.getChannel(this.getGroup(groupName)));
        } else {
            throw new NoSuchElementException("Group does not exist");
        }
    }

    private boolean groupContains( final RPRole role, final String groupName) {
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
    public void createGroup( final String groupName, final RPRole.RoleType type) {
        if (this.groupExists(groupName)) {
            throw new IllegalArgumentException("Group already exists");
        } else {
            final RPChannel channel;
            if (type.equals(RPRole.RoleType.SOUNDTRACK)) {
                channel = this.mixer.createSidechained(this.channelLinker.getChannel(this.groupMap.keySet().stream()
                        .filter(k -> "Speech".equals(k.getTitle())).findAny().orElseThrow()));
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
    public void addClip( final RPPart.PartType type, final String title, final Optional<String> description, final String channel, final Double time,
                        final Double duration, final Optional<File> content) throws ImportException, IllegalArgumentException {
        if (this.clipLinker.clipExists(title)) {
            throw new IllegalArgumentException("Clip already exists");
        } else if ("".equals(title)) {
            throw new IllegalArgumentException("Title is mandatory");
        }
        RPClip<?> clip;
        if (content.isPresent()) {
            try {
                clip = new SampleClip(title, content.get());
            } catch (FileFormatException | OperationUnsupportedException | IOException exception) {
                throw new ImportException("Error in loading file");
            }
        } else {
            clip = new EmptyClip(title, duration);
        }
        final RPPart part = this.createPart(type, title, description);
        this.channelLinker.getTapeChannel(channelLinker.getRole(channel)).insertRPClip(clip, time);
        this.clipLinker.addClipReferences(clip, part);
        this.updateProjectLength();
    }

    private RPPart createPart( final RPPart.PartType type, final String title, final Optional<String> description) {
        if (type.equals(RPPart.PartType.SPEECH)) {
            return description.map(s -> new SpeechPart(title, s)).orElseGet(() -> new SpeechPart(title));
        } else if (type.equals(RPPart.PartType.EFFECTS)) {
            return description.map(s -> new EffectsPart(title, s)).orElseGet(() -> new EffectsPart(title));
        } else {
            return description.map(s -> new SoundtrackPart(title, s)).orElseGet(() -> new SoundtrackPart(title));
        }
    }

    /**
     * This method adds a content to a Clip.
     *
     * @param clip   the title of the Clip
     * @param content the content to put into the Clip
     * @throws NoSuchElementException if no Clip with the given title exists
     */
    @Override
    public void addFileToClip( final String clip, final File content)
            throws ImportException, NoSuchElementException, ClipNotFoundException {
        if (!this.clipLinker.clipExists(clip)) {
            throw new NoSuchElementException("The Clip does not exist");
        }
        final String channel = this.getClipChannel(clip);
        final double clipTimeIn = this.getClipTime(clip,channel);
        final RPPart part = this.clipLinker.getPart(clip);
        if (!this.clipLinker.getClipFromPart(this.clipLinker.getPart(clip)).isEmpty()) {
            this.removeFileFromClip(clip);
        }
        RPClip<?> rpClip = this.getClipFromTitle(clip);
        try {
            this.removeClip(channel, clip, clipTimeIn);
            rpClip = this.clipConverter.fromEmptyToSampleClip((EmptyClip) rpClip, content);
        } catch (OperationUnsupportedException | FileFormatException | IOException exception) {
            throw new ImportException("Error in loading file");
        }
        this.clipLinker.addClipReferences(rpClip,part);
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel)).insertRPClip(rpClip, clipTimeIn);
        this.updateProjectLength();
    }

    /**
     * This method removes the content from a Clip.
     *
     * @param clip the title of the Clip
     * @throws IllegalArgumentException if the Clip has no content
     * @throws NoSuchElementException   if no Clip with the given title exists
     */
    @Override
    public void removeFileFromClip( final String clip)
            throws NoSuchElementException, IllegalArgumentException, ClipNotFoundException {
        if (!this.clipLinker.clipExists(clip)) {
            throw new NoSuchElementException("The Clip does not exist");
        }
        if (!this.clipLinker.getClipFromPart(this.clipLinker.getPart(clip)).getClass().equals(SampleClip.class)) {
            throw new IllegalArgumentException("The Clip has no content");
        }
        final String channel = this.getClipChannel(clip);
        final double clipTimeIn = this.getClipTime(clip,channel);
        final RPPart part = this.clipLinker.getPart(clip);
        RPClip<?> rpClip = this.clipLinker.getClipFromPart(part);
        this.removeClip(channel, clip, clipTimeIn);
        rpClip = this.clipConverter.fromSampleToEmptyClip((SampleClip) rpClip);
        this.clipLinker.addClipReferences(rpClip, part);
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel)).insertRPClip(rpClip, clipTimeIn);
        this.updateProjectLength();
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
    public void removeClip( final String channel, final String clip, final double time) throws ClipNotFoundException {
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel)).removeClip(time);
        this.clipLinker.removeClip(this.getClipLinker().getPart(clip));
        this.updateProjectLength();
    }

    /**
     * A method to return the {@link RPRole} associated with a groupName.
     *
     * @param groupName the name of the Group
     * @return the {@link RPRole}
     * @throws NoSuchElementException if the group does non exist
     */
    @Override
    public RPRole getGroup( final String groupName) throws NoSuchElementException {
        return this.groupMap.keySet().stream()
                .filter(k -> k.getTitle().equals(groupName))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Group does not exist"));
    }

    /**
     * A method to return the list of Channel associated to a group.
     *
     * @param groupName the name of the group
     * @return a List of {@link RPRole}
     * @throws NoSuchElementException if the group does not exist
     */
    @Override
    public List<RPRole> getGroupList( final String groupName) throws NoSuchElementException {
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

    /**
     * @return the {@link RPMixer} of this manager
     */
    @Override
    @JsonIgnore
    public RPMixer getMixer() {
        return this.mixer;
    }

    @Override
    @JsonIgnore
    public List<RPRole> getRoles() {
        return this.channelLinker.getRolesAndGroups().stream()
                .filter(k -> !this.groupMap.containsKey(k))
                .collect(Collectors.toList());
    }

    @Override
    public List<RPPart> getPartList( final String channel) {
        final List<RPPart> list = new ArrayList<>();
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel)).getClipWithTimeIterator()
                .forEachRemaining(p -> list.add(this.clipLinker.getPartFromClip(p.getValue())));
        return list;
    }

    /**
     * @param clip the name of the clip
     * @return the start time of a clip
     */
    @Override
    public Double getClipTime( final String clip, final String channel) {
        final var it =
                this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel)).getClipWithTimeIterator();
        while (it.hasNext()) {
            final var h = it.next();
            if (h.getValue().equals(this.clipLinker.getClipFromPart(this.clipLinker.getPart(clip)))) {
                return h.getKey();
            }
        }
        return null;
    }

    /**
     * @param clip the clip in question
     * @return the duration of a clip
     */
    @Override
    public Double getClipDuration( final String clip) {
        return this.clipLinker.getClipFromPart(this.clipLinker.getPart(clip)).getDuration();
    }

    @Override
    public double getProjectLength() {
        return projectLength;
    }

    @Override
    public void moveClip( final String clip, final String channel, final Double finalTimeIn) throws ClipNotFoundException {
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel))
                .move(this.getClipTime(clip,channel),finalTimeIn);
        this.updateProjectLength();
    }

    @Override
    public void setClipTimeIn( final String clip, final String channel, final Double finalTimeIn) throws ClipNotFoundException {
    	this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel))
                .setTimeIn(this.getClipTime(clip,channel),finalTimeIn);
        this.updateProjectLength();
    }

    @Override
    public void setClipTimeOut( final String clip, final String channel, final Double finalTimeOut) throws ClipNotFoundException {
        this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel))
                .setTimeOut(this.getClipTime(clip,channel),finalTimeOut);
        this.updateProjectLength();
    }

    @Override
    public void splitClip( final String clip, final String channel, final Double splittingTime) throws ClipNotFoundException {
        final String newClip = clip + "(Duplicate)";
        final Double time = this.getClipTime(clip, channel);
        final RPPart part = this.clipLinker.getPart(clip);
        final RPPart newPart = this.createPart(part.getType(), newClip, part.getDescription());
        final RPTapeChannel tapeChannel = this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel));
        tapeChannel.split(this.getClipTime(clip, channel), splittingTime);
        this.clipLinker.addClipReferences(tapeChannel.getClipAt(time).get().getValue(), newPart);
    }

    @Override
    public RPClip<?> getClipFromTitle( final String title) {
        return this.getClipLinker().getClipFromPart(this.getClipLinker().getPart(title));
    }

    @Override
    public RPChannel getChannelFromTitle( final String title) {
        return this.channelLinker.getChannel(this.channelLinker.getRole(title));
    }

    @Override
    public void addSection( final String title, final Optional<String> description, final Double initialTime, final Double duration)
            throws IllegalArgumentException {
        final RPSection section = this.createSection(title, description, duration);
        final boolean flag = this.timeline.addSection(initialTime, section);
        if (!flag) {
            throw new IllegalArgumentException("Incompatible arguments for creation of section");
        }
    }

    private RPSection createSection( final String title, final Optional<String> description, final Double duration) {
        return description.map(d -> new SectionImpl(title,d,duration)).orElseGet(() -> new SectionImpl(title,duration));
    }

    @Override
    public void removeSection( final Double time) throws NoSuchElementException{
        if (this.timeline.getSection(time).isEmpty()) {
            throw new NoSuchElementException("No section present at that time");
        } else {
            this.timeline.removeSection(this.timeline.getSection(time).get());
        }
    }

    @Override
    @JsonIgnore
    public Set<Map.Entry<Double, RPSection>> getSections() {
        return this.timeline.getAllSections();
    }

    @Override
    public void updateProjectLength() {
        try {
            if (this.furthestClipTime() + MIN_SPACING > MIN_LENGTH) {
                this.projectLength = this.furthestClipTime() + MIN_SPACING;
            }
        } catch (ClipNotFoundException e) {
            this.projectLength = MIN_LENGTH;
        }
    }

    private Double furthestClipTime() throws ClipNotFoundException {
        double time = 0.0;
        for (final var r : this.getRoles()) {
            final var i = this.channelLinker.getTapeChannel(r).getClipWithTimeIterator();
            while (i.hasNext()) {
                final var clip = i.next().getValue();
                if (Double.compare(this.clipEndTime(clip),time) > 0) {
                    time = this.clipEndTime(clip);
                }
            }
        }
        return time;
    }

    private Double clipEndTime( final RPClip<?> clip) throws ClipNotFoundException {
        final String channel = this.getClipChannel(clip.getTitle());
        return this.channelLinker.getTapeChannel(this.channelLinker.getRole(channel))
                .getClipTimeOut(this.getClipTime(clip.getTitle(), channel));
    }

    @Override
    public String getClipChannel( final String clip) {
        for (final var r : this.getRoles()) {
            final var i = this.channelLinker.getTapeChannel(r)
                    .getClipWithTimeIteratorFiltered(c -> c.getValue().getTitle().equals(clip));
            if (i.hasNext()) {
                return r.getTitle();
            }
        }
        return null;
    }

    @Override
    public Speaker createSpeaker( final int id, final String firstName, final String lastName) {
        return new SimpleSpeaker.Builder(id).firstName(firstName).lastName(lastName).build();
    }

    @Override
    public void addSpeakerToRubric( final Speaker speaker) {
        this.rubric.addSpeaker(speaker);
    }

    @Override
    public void removeSpeakerFromRubric( final Speaker speaker) {
        this.rubric.removeSpeaker(speaker);
    }

    @Override
    public List<Speaker> getSpeakersInRubric() {
        return this.rubric.getSpeakers();
    }

}
