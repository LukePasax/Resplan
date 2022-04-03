package daw.core.channel;

import daw.general.Volume;
import net.beadsproject.beads.ugens.Gain;

import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a basic implementation of {@link RPChannel}, which may be extended if needed.
 * This implementation initializes a channel so that it does not possess an input -
 * the only way to plug in an input is to call the method which does that.
 * Note that the presence of a {@link ProcessingUnit} is merely optional,as a channel is functionally
 * just a pipe through which an audio stream flows.
 * A channel can be of one and only one Type, which is immutable and must be declared upon initialization.
 */
public class BasicChannel implements RPChannel {

    private final Volume vol;
    private final Type type;
    private final Optional<ProcessingUnit> pu;
    private Optional<Gain> inputGain;
    private boolean enabled;

    protected BasicChannel(final Volume vol, final Type type, ProcessingUnit pu) {
        this.vol = vol;
        this.type = type;
        this.pu = Optional.of(Objects.requireNonNull(pu));
        this.inputGain = Optional.empty();
    }

    @Override
    public void addInput(Gain g) {
        this.inputGain = Optional.of(Objects.requireNonNull(g));
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
