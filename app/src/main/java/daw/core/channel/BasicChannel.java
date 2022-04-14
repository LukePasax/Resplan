package daw.core.channel;

import Resplan.AudioContextManager;
import daw.general.Volume;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a basic implementation of {@link RPChannel}, which may be extended if needed.
 * This implementation initializes a channel so that it does not possess an input -
 * the only way to plug in an input is to call the method which does that.
 * Note that the presence of a {@link ProcessingUnit} is merely optional, as a channel is functionally
 * just a pipe through which an audio stream flows.
 * A channel can be of one and only one Type, which is immutable and must be declared upon initialization.
 */
public class BasicChannel implements RPChannel {

    private final Volume vol;
    private final Panner pan;
    private final Type type;
    private final Optional<ProcessingUnit> pu;
    private Optional<Gain> inputGain;
    private boolean enabled;

    protected BasicChannel(final Volume vol, Panner pan, final Type type, ProcessingUnit pu) {
        this.vol = vol;
        this.pan = pan;
        this.type = type;
        this.pu = (pu == null) ? Optional.of(pu) : Optional.empty();
        this.inputGain = Optional.empty();
    }

    @Override
    public void addInput(Gain g) {
        this.inputGain = Optional.of(Objects.requireNonNull(g));
    }

    @Override
    public Gain getOutput() {
        if (this.inputGain.isPresent()) {
            if (this.isProcessingUnitPresent()) {
                final var g = new Gain(AudioContextManager.getAudioContext(), 1);
                this.pu.get().connect(g);
                return g;
            }
            return this.inputGain.get();
        }
        throw new IllegalStateException("Cannot produce output if no input has been provided.");
    }

    @Override
    public void setVolume(int vol) {
        this.vol.setVolume(vol);
    }

    @Override
    public int getVolume() {
        return this.vol.getVolume();
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    public boolean isProcessingUnitPresent() {
        return this.pu.isPresent();
    }
}
