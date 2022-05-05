package daw.core.channel;

import Resplan.AudioContextManager;
import daw.core.audioprocessing.ProcessingUnit;
import daw.general.Volume;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

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

    private final Volume vol;
    private final Panner pan;
    private final Type type;
    private Optional<ProcessingUnit> pu;
    private final Gain gainIn;
    private final Gain gainOut;
    private boolean enabled;

    /**
     * Constructs a channel with the given type and parameters. The channel is initially enables and has
     * no {@link ProcessingUnit}.
     * @param vol a {@link Volume}.
     * @param pan a {@link Panner}.
     * @param type a {@link Type}.
     */
    protected BasicChannel(final Volume vol, final Panner pan, final Type type) {
        this.vol = vol;
        this.pan = pan;
        this.type = type;
        this.enabled = true;
        this.pu = Optional.empty();
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), 2);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), 2);
        this.setStructure();
    }

    private void setStructure() {
        this.gainOut.addInput(pan);
        this.pan.addInput(gainIn);
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
     * @throws IllegalStateException if the channel is not enabled or if no input has been provided.
     */
    @Override
    public Gain getOutput() {
        if (this.isEnabled()) {
            return this.gainOut;
        } else {
            throw new IllegalStateException("Cannot produce output.");
        }
    }

    /**
     * {@inheritDoc}
     * @param vol the value that the volume must be set to.
     */
    @Override
    public void setVolume(int vol) {
        this.vol.setVolume(vol);
    }

    /**
     * {@inheritDoc}
     * @return an integer representing the volume.
     */
    @Override
    public int getVolume() {
        return this.vol.getVolume();
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disable() {
        this.enabled = false;
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
        this.pu = Optional.of(pu);
        this.pu.get().addInput(gainIn);
        this.pan.clearInputConnections();
        this.pu.get().connect(this.pan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeProcessingUnit() {
        if (this.isProcessingUnitPresent()) {
            this.pu = Optional.empty();
            this.setStructure();
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

}
