package daw.core.channel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a basic implementation of {@link RPChannel}, which may be extended if needed.
 * Note that the presence of a {@link ProcessingUnit} is merely optional, as a channel is functionally
 * just a pipe through which an audio stream flows.
 * Moreover, the channel is thought to initially have no {@link ProcessingUnit}; thereby, the only way to
 * add one is to call the method which does that.
 * A channel can be of one and only one Type, which is immutable and must be declared upon initialization.
 */
public class BasicChannel implements RPChannel {

    private static final float DEFAULT_GAIN_IN = 0.9f;

    private final Panner pan;
    private final Type type;
    private Optional<ProcessingUnit> pu;
    private final Gain gainIn;
    private final Gain gainOut;
    private final Gain gainMute;
    private boolean enabled;

    /**
     * Constructs a channel with the given type and parameters. The channel is initially enables and has
     * no {@link ProcessingUnit}.
     * @param type a {@link Type}.
     */
    protected BasicChannel(@JsonProperty("type") final Type type) {
        this(type, null);
    }

    @JsonCreator
    private BasicChannel(@JsonProperty("type") final Type type, @JsonProperty("processingUnit")
                        final ProcessingUnit processingUnit) {
        this.pan = new Panner(AudioContextManager.getAudioContext());
        this.type = type;
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), 1, DEFAULT_GAIN_IN);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), 1, 1.0f);
        // channel is initially enabled
        this.enabled = true;
        this.gainMute = new Gain(AudioContextManager.getAudioContext(), 1, 1.0f);
        this.setStructure();
        // processing unit is present only after deserialization
        this.pu = Optional.empty();
        if (processingUnit != null) {
            this.addProcessingUnit(processingUnit);
        }
    }

    private void setStructure() {
        // in -> pan -> mute -> out
        this.pan.addInput(this.gainIn);
        this.gainMute.addInput(this.pan);
        this.gainOut.addInput(this.gainMute);
    }

    /**
     * {@inheritDoc}
     * @param in the source that must be connected.
     */
    @Override
    public void connectSource(UGen in) {
        this.gainIn.addInput(in);
    }

    /**
     * {@inheritDoc}
     * @param u the source that must be disconnected.
     */
    @Override
    public void disconnectSource(UGen u) {
        this.gainIn.removeAllConnections(u);
    }

    /**
     * {@inheritDoc}
     * @return a {@link Gain} that represents the output.
     */
    @Override
    @JsonIgnore
    public Gain getOutput() {
        return this.gainOut;
    }

    /**
     * {@inheritDoc}
     * @param vol the value that the volume must be set to. This value has to be between 0 and 100.
     * @throws IllegalArgumentException if the volume is not between 0 and 100.
     */
    @Override
    public void setVolume(int vol) {
        if (vol < 0 || vol > 100) {
            throw new IllegalArgumentException("Volume level must be between 0 and 100.");
        }
        this.gainOut.setGain((float) vol/100);
    }

    /**
     * {@inheritDoc}
     * @return an integer representing the volume.
     */
    @Override
    public int getVolume() {
        return (int) Math.floor(this.gainOut.getGain() * 100);
    }

    /**
     * {@inheritDoc}
     * @return a {@link Panner}.
     */
    public Panner getPanner() {
        return this.pan;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enable() {
        this.enabled = true;
        this.gainMute.setGain(1.0f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disable() {
        this.enabled = false;
        this.gainMute.setGain(0.0f);
    }

    /**
     * {@inheritDoc}
     * @return true if the channel is enabled, that means it can produce output.
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * {@inheritDoc}
     * @param pu the {@link ProcessingUnit} to be added.
     */
    public void addProcessingUnit(ProcessingUnit pu) {
        // in -> pu -> pan -> mute -> out
        if (!this.isProcessingUnitPresent()) {
            this.pu = Optional.of(pu);
            this.pu.get().addInput(this.gainIn);
            this.pan.clearInputConnections();
            this.pu.get().connect(this.pan);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeProcessingUnit() {
        if (this.isProcessingUnitPresent()) {
            this.pu = Optional.empty();
            this.pan.clearInputConnections();
            this.pan.addInput(this.gainIn);
        }
    }

    /**
     *
     * @return an {@link Optional} containing the {@link ProcessingUnit} if it is present,
     * otherwise an empty {@link Optional}.
     */
    @Override
    public Optional<ProcessingUnit> getProcessingUnit() {
        return this.pu;
    }

    /**
     * {@inheritDoc}
     * @return true if the channel contains a {@link ProcessingUnit}.
     */
    @Override
    public boolean isProcessingUnitPresent() {
        return this.pu.isPresent();
    }

    /**
     *
     * @return the type of the channel.
     */
    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "BasicChannel{" +
                "type=" + type +
                ", gainOut=" + gainOut +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicChannel that = (BasicChannel) o;
        return enabled == that.enabled &&
                Objects.equals(pan, that.pan) &&
                type == that.type &&
                Objects.equals(pu, that.pu) &&
                Objects.equals(gainIn, that.gainIn) &&
                Objects.equals(gainOut, that.gainOut) &&
                Objects.equals(gainMute, that.gainMute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pan, type, pu, gainIn, gainOut, gainMute, enabled);
    }

}
