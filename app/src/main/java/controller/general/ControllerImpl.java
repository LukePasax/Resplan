package controller.general;

import controller.storing.RPFileReader;
import controller.storing.Writer;
import controller.storing.RPFileWriter;
import daw.core.audioprocessing.*;
import daw.core.clip.ClipNotFoundException;
import daw.core.clip.RPClip;
import daw.core.clip.RPRecorder;
import daw.core.clip.Recorder;
import daw.engine.Engine;
import daw.engine.RPEngine;
import daw.manager.ChannelLinker;
import daw.manager.ImportException;
import daw.manager.Manager;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.AudioFileType;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.ugens.RecordToSample;
import planning.Element;
import planning.RPPart;
import planning.RPRole;
import planning.Speaker;
import planning.Text;
import planning.TextFactoryImpl;
import resplan.Starter;
import view.common.AlertDispatcher;
import view.common.App;
import view.common.ViewDataImpl;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class ControllerImpl implements Controller {

    private static final String SPEECH_TYPE = "SPEECH";
    private static final String EFFECTS_TYPE = "EFFECTS";
    private static final String SOUNDTRACK_TYPE = "SOUNDTRACK";

    private final ProjectDownloader downloader;
    private final ProjectLoader loader;
    private Manager manager;
    private App app;
    private RPEngine engine;
    private File currentProject;
    private final File appSettings = new File(WORKING_DIRECTORY + SEP + APP_SETTINGS);
    private RPRecorder recorder;
    private RecordToSample exporter;
    private final Set<String> mutedChannels = new HashSet<>();
    private final Set<String> soloChannels = new HashSet<>();
    private boolean solo;

    /**
     * Sets up the application.
     */
    public ControllerImpl() {
        this.loader = new ProjectLoaderImpl();
        this.downloader = new ProjectDownloaderImpl();
        this.startApp();
        this.manager.getMixer().connectToSystem();
        this.engine = new Engine((ChannelLinker) this.manager.getChannelLinker());
    }

    /**
     * {@inheritDoc}
     * @param app the {@link App}.
     */
    @Override
    public void setApp(final App app) {
        this.app = app;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startApp() {
        try {
            // tries to load the template project
            final var fileName = new RPFileReader(this.appSettings).read();
            if (fileName.isBlank()) {
                // if no template project is set, then it opens an empty project
                this.currentProject = null;
                this.manager = new Manager();
            } else {
                this.currentProject = new File(fileName);
                this.manager = this.loader.load(this.currentProject);
            }
        } catch (IOException | FileFormatException e) {
            this.manager = new Manager();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newProject() {
        this.startApp();
        this.loadViewData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadViewData() {
        App.getData().clearData();
        this.manager.getRoles().forEach(c -> {
            App.getData().addChannel(new ViewDataImpl.Channel(c.getTitle(), c.getType().name()));
            this.manager.getPartList(c.getTitle()).forEach(p -> {
                final var clip = this.manager.getClipFromTitle(p.getTitle());
                if (clip.isEmpty()) {
                    App.getData().addClip(App.getData().getChannel(c.getTitle()), new ViewDataImpl.Clip(
                            p.getTitle(), this.manager.getClipTime(p.getTitle(), c.getTitle()), clip.getDuration(),
                            Optional.empty(), Optional.empty(), Optional.empty()));
                } else {
                    App.getData().addClip(App.getData().getChannel(c.getTitle()), new ViewDataImpl.Clip(
                            p.getTitle(), this.manager.getClipTime(p.getTitle(), c.getTitle()), clip.getDuration(),
                            Optional.of(clip.getContentPosition()), Optional.of(clip.getContentDuration()), Optional.of(new File(((Sample) clip.getContent()).getFileName()).getName())));
                }
            });
        });
        this.manager.getSections()
                .forEach(s -> App.getData().addSection(new ViewDataImpl.Section(s.getValue().getTitle(), s.getKey())));
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @throws DownloadingException if an error has occurred when trying to write to file.
     * @throws IllegalStateException if the current project has never been saved before.
     */
    @Override
    public void save() throws DownloadingException, IllegalStateException {
        if (this.currentProject == null) {
            throw new IllegalStateException("Select a file name and a directory.");
        } else {
            this.saveCurrentProject();
        }
    }

    private void saveCurrentProject() throws DownloadingException {
        try {
            this.downloader.download(this.currentProject, this.manager);
        } catch (IOException | FileFormatException e) {
            throw new DownloadingException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param file the file where to save.
     * @throws DownloadingException if an error has occurred when trying to write to file.
     */
    @Override
    public void saveWithName(final File file) throws DownloadingException {
        this.currentProject = file;
        this.saveCurrentProject();
    }

    /**
     * {@inheritDoc}
     * @param file the file where to read.
     * @throws LoadingException if an error has occurred when trying to read from file.
     */
    @Override
    public void openProject(final File file) throws LoadingException {
        try {
            this.manager = this.loader.load(file);
            this.currentProject = file;
            this.manager.getMixer().connectToSystem();
            this.engine = new Engine((ChannelLinker) this.manager.getChannelLinker());
            this.loadViewData();
        } catch (IOException | FileFormatException e) {
            throw new LoadingException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @throws DownloadingException if the writing to file is unsuccessful.
     * @throws IllegalStateException if the current project has never been saved before.
     */
    @Override
    public void setTemplateProject() throws DownloadingException, IllegalStateException {
        if (this.currentProject == null) {
            throw new IllegalStateException("Save project before setting it as template.");
        }
        try {
            final Writer writer = new RPFileWriter(this.appSettings);
            writer.write(this.currentProject.getAbsolutePath());
        } catch (IOException e) {
            throw new DownloadingException("Unable to perform this operation. "
                    + "Retry to set this project as the template.");
        }
    }

    /**
     * {@inheritDoc}
     * @throws DownloadingException {@inheritDoc}
     */
    @Override
    public void resetTemplateProject() throws DownloadingException {
        final Writer writer = new RPFileWriter(this.appSettings);
        try {
            writer.write("");
        } catch (IOException e) {
            throw new DownloadingException("Unable to perform this operation. Retry to reset the template project.");
        }
    }

    /**
     * {@inheritDoc}
     * @param type the type of the new role.
     * @param title the title of the new role.
     * @param description the description of new the role. If the description is empty, then the
     *                    role will have no description in model.
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public void newChannel(final String type, final String title, final String description) throws IllegalArgumentException {
        RPRole.RoleType roleType;
        if (SPEECH_TYPE.equals(type)) {
            roleType = RPRole.RoleType.SPEECH;
        } else if (EFFECTS_TYPE.equals(type)) {
            roleType = RPRole.RoleType.EFFECTS;
        } else {
            roleType = RPRole.RoleType.SOUNDTRACK;
        }
        final Optional<String> desc = "".equals(description) ? Optional.empty() : Optional.of(description);
        this.manager.addChannel(roleType, title, desc);
        App.getData().addChannel(new ViewDataImpl.Channel(title, type));
    }

    /**
     * {@inheritDoc}
     * @param type the type of the new part.
     * @param title the title of the new part.
     * @param description the description of new the part. If the description is empty, then the
     *                    part will have no description in model.
     * @param channel the channel this clip has to be put into.
     * @param time the position of the clip in the {@link planning.RPTimeline}.
     * @param duration the length of the clip.
     * @param content the content that has to be put into this clip. If the given content is null, then this clip
     *                will initially have no content. To later add content into a clip,
     *                use {@link #addContentToClip(String, File)}
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws ImportException {@inheritDoc}
     */
    @Override
    public void newClip(final String type, final String title, final String description, final String channel, final Double time,
                        final Double duration, final File content) throws IllegalArgumentException, ImportException {
        RPPart.PartType partType;
        if (SPEECH_TYPE.equals(type)) {
            partType = RPPart.PartType.SPEECH;
        } else if (EFFECTS_TYPE.equals(type)) {
            partType = RPPart.PartType.EFFECTS;
        } else {
            partType = RPPart.PartType.SOUNDTRACK;
        }
        final Optional<String> desc = "".equals(description) ? Optional.empty() : Optional.of(description);
        final Optional<File> file = content == null ? Optional.empty() : Optional.of(content);
        this.manager.addClip(partType, title, desc, channel, time, duration, file);
        final RPClip<?> clip = this.manager.getClipFromTitle(title);
        if (clip.isEmpty()) {
            App.getData().addClip(App.getData().getChannel(channel), new ViewDataImpl.Clip(title, time, duration,
                    Optional.empty(), Optional.empty(), Optional.empty()));
        } else {
            App.getData().addClip(App.getData().getChannel(channel), new ViewDataImpl.Clip(title, time,
                    this.manager.getClipDuration(title), Optional.of(clip.getContentPosition()),
                    Optional.of(clip.getContentDuration()),
                    Optional.of(new File(((Sample) clip.getContent()).getFileName()).getName())));
        }
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @param title the name of the channel to be deleted.
     */
    @Override
    public void deleteChannel(final String title) {
        try {
            this.manager.removeChannel(title);
            App.getData().removeChannel(App.getData().getChannel(title));
        } catch (NoSuchElementException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @param title the name of a clip.
     * @param channel the name of a channel.
     * @param time a position in the timeline.
     */
    @Override
    public void deleteClip(final String title, final String channel, final Double time) {
        try {
            this.manager.removeClip(channel, title, time);
            App.getData().removeClip(App.getData().getChannel(channel), App.getData().getClip(channel, title));
        } catch (NoSuchElementException | ClipNotFoundException e) {
            AlertDispatcher.dispatchError(e.getLocalizedMessage());
        }
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getChannelList() {
        return this.manager.getRoles().stream().map(Element::getTitle).collect(Collectors.toList());
    }

    @Override
    public List<String> getClipList(final String channel) {
        return this.manager.getPartList(channel).stream().map(Element::getTitle).collect(Collectors.toList());
    }

    @Override
    public Optional<Sample> getClipSample(final String clip) {
        try {
            final Sample sample = (Sample) this.manager.getClipFromTitle(clip).getContent();
            return Optional.of(sample);
        } catch (UnsupportedOperationException e) {
            return Optional.empty();
        }
    }

    @Override
    public Double getClipTime(final String clip, final String channel) {
        return this.manager.getClipTime(clip, channel);
    }

    @Override
    public Double getClipDuration(final String clip) {
        return this.manager.getClipDuration(clip);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
    	AudioContextManager.getAudioContext().start();
        this.engine.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        this.engine.pause();
        AudioContextManager.getAudioContext().stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.engine.stop();
        AudioContextManager.getAudioContext().stop();
    }

    /**
     * {@inheritDoc}
     * @param time a position in the timeline.
     */
    @Override
    public void setPlaybackTime(final Double time) {
        this.engine.setPlaybackTime(time);
        this.updatePlaybackTime(time);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Double getPlaybackTime() {
        return this.engine.getPlaybackTime();
    }

    /**
     * {@inheritDoc}
     * @param time a position in the timeline.
     */
    @Override
    public void updatePlaybackTime(final Double time) {
        this.app.updatePlaybackTime(time);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isPaused() {
        return this.engine.isPaused();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public double getProjectLength() {
        return this.manager.getProjectLength();
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param finalTimeIn the new starting position of the clip.
     * @throws ClipNotFoundException {@inheritDoc}
     */
    @Override
    public void moveClip(final String clip, final String channel, final Double finalTimeIn) throws ClipNotFoundException {
        //App.getData().removeClip(App.getData().getChannel(channel),App.getData().getClip(channel,clip));
        this.manager.moveClip(clip, channel, finalTimeIn);
        //createClipView(clip, channel);
        updateChannelClipsView(channel);
        App.getData().setProjectLenght(this.getProjectLength());
    }

    private void updateChannelClipsView(final String channel) {
    	App.getData().clearChannelClips(App.getData().getChannel(channel));
        this.manager.getPartList(channel).forEach(p -> this.createClipView(p.getTitle(), channel));
    }

    private void createClipView(final String clip, final String channel) {
        final Double time = this.manager.getClipTime(clip, channel);
        final Double duration = this.manager.getClipDuration(clip);
        final RPClip<?> rpClip = this.manager.getClipFromTitle(clip);
        if (rpClip.isEmpty()) {
            App.getData().addClip(App.getData().getChannel(channel), new ViewDataImpl.Clip(clip, time, duration,
                    Optional.empty(), Optional.empty(), Optional.empty()));
        } else {
            App.getData().addClip(App.getData().getChannel(channel), new ViewDataImpl.Clip(clip, time, duration,
                    Optional.of(rpClip.getContentPosition()), Optional.of(rpClip.getContentDuration()),
                    Optional.of(new File(((Sample) rpClip.getContent()).getFileName()).getName())));
        }
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param finalTimeIn the starting position of the clip.
     * @throws ClipNotFoundException {@inheritDoc}
     */
    @Override
    public void setClipTimeIn(final String clip, final String channel, final Double finalTimeIn) throws ClipNotFoundException {
    	//App.getData().removeClip(App.getData().getChannel(channel),App.getData().getClip(channel,clip));
    	this.manager.setClipTimeIn(clip, channel, finalTimeIn);
    	this.updateChannelClipsView(channel);
        //createClipView(clip, channel);
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param finalTimeOut the timeout position of the clip.
     * @throws ClipNotFoundException {@inheritDoc}
     */
    @Override
    public void setClipTimeOut(final String clip, final String channel, final Double finalTimeOut) throws ClipNotFoundException {
        //App.getData().removeClip(App.getData().getChannel(channel),App.getData().getClip(channel,clip));
        this.manager.setClipTimeOut(clip, channel, finalTimeOut);
        //createClipView(clip, channel);
        this.updateChannelClipsView(channel);
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @param channel the channel of the clip.
     * @param splittingTime the position in the timeline at which the clip has to be split.
     * @throws ClipNotFoundException {@inheritDoc}
     */
    @Override
    public void splitClip(final String clip, final String channel, final Double splittingTime) throws ClipNotFoundException {
        this.manager.splitClip(clip, channel, splittingTime);
        this.updateChannelClipsView(channel);
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @param content the {@link File} representing the content to be added.
     * @throws ImportException {@inheritDoc}
     * @throws ClipNotFoundException {@inheritDoc}
     */
    @Override
    public void addContentToClip(final String clip, final File content) throws ImportException, ClipNotFoundException {
        final String channel = this.manager.getClipChannel(clip);
        App.getData().removeClip(App.getData().getChannel(channel), App.getData().getClip(channel, clip));
        this.manager.addFileToClip(clip, content);
        this.createClipView(clip, channel);
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @throws ClipNotFoundException {@inheritDoc}
     */
    @Override
    public void removeContentFromClip(final String clip) throws ClipNotFoundException {
        final String channel = this.manager.getClipChannel(clip);
        App.getData().removeClip(App.getData().getChannel(channel), App.getData().getClip(channel, clip));
        this.manager.removeFileFromClip(clip);
        this.createClipView(clip, channel);
        App.getData().setProjectLenght(this.getProjectLength());
    }

    /**
     * {@inheritDoc}
     * @param title the name of the section.
     * @param description the description of the section. If the description is empty, then the
     *                    part will have no description in model.
     * @param initialTime the position of the section in the timeline.
     * @param duration the duration of the section.
     */
    @Override
    public void newSection(final String title, final String description, final Double initialTime, final Double duration) {
        final Optional<String> desc = "".equals(description) ? Optional.empty() : Optional.of(description);
        this.manager.addSection(title, desc, initialTime, duration);
        App.getData().addSection(new ViewDataImpl.Section(title, initialTime));
    }

    @Override
    public void deleteSection(final Double time) {
        this.manager.removeSection(time);
        App.getData().removeSection(App.getData().getUnmodifiableSections().stream()
                .filter(s -> s.getPosition().equals(time))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No section present at that time")));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startRecording() {
        this.recorder = new Recorder();
        this.recorder.record();
    }

    /**
     * {@inheritDoc}
     * @param clip the name of the clip the recording has to be put into.
     * @param file the file the recording has to be saved on.
     * @throws ImportException {@inheritDoc}
     * @throws ClipNotFoundException {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void stopRecording(final String clip, final File file) throws ImportException, ClipNotFoundException, IOException {
        this.recorder.pause();
        this.recorder.getSample().write(file.getAbsolutePath(), AudioFileType.WAV);
        this.addContentToClip(clip, file);
    }

    /**
     * {@inheritDoc}
     * @param startTime the position in the timeline from which export has to start.
     */
    @Override
    public void startExport(final Double startTime) {
        final AudioContext ac = AudioContextManager.getAudioContext();
        final Sample sample = new Sample(0);
        this.exporter = new RecordToSample(ac, sample, RecordToSample.Mode.INFINITE);
        this.exporter.addInput(this.manager.getMixer().getMasterChannel().getOutput());
        ac.out.addDependent(this.exporter);
        ac.out.removeAllConnections(this.manager.getMixer().getMasterChannel().getOutput());
        this.setPlaybackTime(startTime);
        this.start();
        this.exporter.start();
    }

    /**
     * {@inheritDoc}
     * @param file the file the audio content of the project has to be put into.
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void stopExport(final File file) throws IOException {
        final AudioContext ac = AudioContextManager.getAudioContext();
        this.exporter.pause(true);
        this.stop();
        ac.out.addInput(this.manager.getMixer().getMasterChannel().getOutput());
        ac.out.removeDependent(this.exporter);
        this.exporter.clip();
        this.exporter.getSample().write(file.getAbsolutePath(), AudioFileType.WAV);
    }

    /**
     * {@inheritDoc}
     * @param channel the name of the channel that has to be enabled or disabled.
     */
    @Override
    public void setMute(final String channel) {
        if (this.soloChannels.contains(channel)) {
            throw new IllegalStateException("Cannot mute a solo channel. Remove solo from it, then call this method.");
        }
        if (this.mutedChannels.contains(channel)) {
            this.mutedChannels.remove(channel);
            App.getData().getChannel(channel).setMute(false);
        } else {
            this.mutedChannels.add(channel);
            App.getData().getChannel(channel).setMute(true);
        }
        this.manageMute();
    }

    /**
     * {@inheritDoc}
     * @param channel The name of the channel to be set as solo.
     */
    @Override
    public void setSolo(final String channel) {
        if (this.mutedChannels.contains(channel)) {
            throw new IllegalStateException("Cannot solo a muted channel. Unmute it first, then call this method.");
        }
        if (this.soloChannels.contains(channel)) {
            this.removeSolo(channel);
        } else {
            this.addSolo(channel);
        }
        this.manageMute();
    }

    private void addSolo(final String channel) {
        this.solo = true;
        this.soloChannels.add(channel);
        App.getData().getChannel(channel).setSolo(true);
    }

    private void removeSolo(final String channel) {
        this.soloChannels.remove(channel);
        App.getData().getChannel(channel).setSolo(false);
        if (this.soloChannels.isEmpty()) {
            this.solo = false;
        }
    }

    private void manageMute() {
        if (this.solo) {
            this.manageMuteInSoloEnvironment();
        } else {
            this.manageMuteInNonSoloEnvironment();
        }
    }

    private void manageMuteInNonSoloEnvironment() {
        this.manager.getRoles().stream()
                .map(Element::getTitle)
                .forEach(i -> {
                    final var channel = this.manager.getChannelFromTitle(i);
                    if (this.mutedChannels.contains(i)) {
                        channel.disable();
                    } else {
                        channel.enable();
                    }
                });
    }

    private void manageMuteInSoloEnvironment() {
        this.manager.getRoles().stream()
                .map(Element::getTitle)
                .forEach(i -> {
                    final var channel = this.manager.getChannelFromTitle(i);
                    if (this.soloChannels.contains(i)) {
                        channel.enable();
                    } else {
                        channel.disable();
                    }
                });
    }

    @Override
    public void addEffectAtPosition(final String channel, final String effect, final int index) {
        this.getProcessingUnit(channel).addEffectAtPosition(this.createEffect(effect), index);
    }

    private RPEffect createEffect(final String effect) {
        if ("Compressor".equals(effect)) {
            return new Compression(2);
        } else if (effect.equals("Limiter")) {
            return new Limiter(2);
        } else if ("Low pass".equals(effect)) {
            return new LowPassFilter(2);
        } else if ("High pass".equals(effect)) {
            return new HighPassFilter(2);
        } else if ("Reverb".equals(effect)) {
            return new DigitalReverb(2);
        } else {
            return new Gate(2);
        }
    }

    @Override
    public void removeEffectAtPosition(final String channel, final int index) {
        this.getProcessingUnit(channel).removeEffectAtPosition(index);
    }

    @Override
    public void moveEffect(final String channel, final int index1, final int index2) {
        this.getProcessingUnit(channel).moveEffect(index1, index2);
    }

    @Override
    public void setEffectParameters(final String channel, final int index, final Map<String, Float> parameters) {
        this.getProcessingUnit(channel).getEffectAtPosition(index).setParameters(parameters);
    }

    @Override
    public Map<String, Float> getEffectParameters(final String channel, final int index) {
        return this.getProcessingUnit(channel).getEffectAtPosition(index).getParameters();
    }

    /**
     * {@inheritDoc}
     * @param channel the name of a channel.
     * @param value the 0 - 100 integer representation of the volume.
     */
    @Override
    public void setVolume(final String channel, final int value) {
        this.manager.getChannelLinker().getChannel(this.manager.getChannelLinker().getRole(channel)).setVolume(value);
    }

    /**
     * {@inheritDoc}
     * @param channel the name of a channel.
     * @param value the -1.0 - 1.0 float representation of the pan.
     */
    @Override
    public void setPan(final String channel, final float value) {
        this.manager.getChannelLinker().getChannel(this.manager.getChannelLinker().getRole(channel)).getPanner().setPos(value);
    }

    /**
     * {@inheritDoc}.
     * @param channel
     * @return {@inheritDoc}
     */
    private ProcessingUnit getProcessingUnit(final String channel) {
        return this.manager.getChannelLinker().getChannel(this.manager.getGroup(channel)).getProcessingUnit().orElseThrow();
    }

    /**
     * {@inheritDoc}
     * @param clipTitle the name of a clip.
     * @param text the text that has to be associated to the given clip.
     */
    @Override
    public void setClipText(final String clipTitle, final String text) {
        this.manager.getClipLinker().getPart(clipTitle).addText(new TextFactoryImpl().createFromString(text));
    }

    /**
     * {@inheritDoc}
     * @param clipTitle the name of a clip.
     * @param fileName the name of the file the text has to be read from.
     * @throws IOException
     */
    @Override
    public void setClipTextFromFile(final String clipTitle, final String fileName) throws IOException {
        this.manager.getClipLinker().getPart(clipTitle).addText(new TextFactoryImpl().createFromFile(fileName));
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @return {@inheritDoc}
     */
    @Override
    public Optional<String> getClipText(final String clip) {
        final Optional<Text> text = this.manager.getClipLinker().getPart(clip).getText();
        return text.map(Text::getContent);
    }

    /**
     * {@inheritDoc}
     * @param clip the name of a clip.
     * @return {@inheritDoc}
     */
    @Override
    public String getClipType(final String clip) {
       final var type = this.manager.getClipLinker().getPart(clip).getType();
       if (type.equals(RPPart.PartType.SPEECH)) {
           return SPEECH_TYPE;
       } else if (type.equals(RPPart.PartType.EFFECTS)) {
           return EFFECTS_TYPE;
       } else {
           return SOUNDTRACK_TYPE;
       }
    }

    @Override
    public void setClipDescription(final String title, final String text) {
        this.manager.getClipLinker().getPartFromClip(this.manager.getClipFromTitle(title)).addDescription(text);
    }

    @Override
    public Optional<String> getClipDescription(final String clip) {
        return this.manager.getClipLinker().getPart(clip).getDescription();
    }

    /**
     * {@inheritDoc}
     * @param channel the name of a channel.
     * @return {@inheritDoc}
     */
    @Override
    public String getChannelType(final String channel) {
        final var type = this.manager.getRoles().stream()
                .filter(c -> c.getTitle().equals(channel))
                .findFirst()
                .orElseThrow().getType();
        if (type.equals(RPRole.RoleType.SPEECH)) {
            return SPEECH_TYPE;
        } else if (type.equals(RPRole.RoleType.EFFECTS)) {
            return EFFECTS_TYPE;
        } else {
            return SOUNDTRACK_TYPE;
        }
    }

    @Override
    public void setChannelDescription(final String title, final String text) {
        this.manager.getRoles().stream()
                .filter(role -> role.getTitle().equals(title))
                .findFirst()
                .orElseThrow()
                .addDescription(text);
    }

    @Override
    public Optional<String> getChannelDescription(final String title) {
        return this.manager.getRoles().stream()
                .filter(role -> role.getTitle().equals(title))
                .findFirst()
                .flatMap(Element::getDescription);
    }

    @Override
    public void addSpeakerToRubric(final int code, final String firstName, final String lastName) {
        this.manager.addSpeakerToRubric(this.manager.createSpeaker(code, firstName, lastName));
    }

    @Override
    public void removeSpeakerFromRubric(final int code, final String firstName, final String lastName) {
        this.manager.removeSpeakerFromRubric(this.manager.createSpeaker(code, firstName, lastName));
    }

    @Override
    public List<Speaker> getSpeakers() {
        return this.manager.getSpeakersInRubric();
    }

    // ONLY FOR TEMPORARY TESTING PURPOSES
    public Manager getManager() {
        return this.manager;
    }

}
