package daw.core.channel;

import Resplan.AudioContextManager;
import daw.general.Volume;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

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
    private final Gain gainIn;
    private final Gain gainOut;
    private boolean enabled;

    protected BasicChannel(final Volume vol, Panner pan, final Type type) {
        this.vol = vol;
        this.pan = pan;
        this.type = type;
        this.pu = Optional.empty();
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), 2);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), 2);
        this.gainOut.addInput(pan);
        this.pan.addInput(gainIn);
    }

    public void addProcessingUnit(ProcessingUnit pu){

    }

    @Override
    public void connectSource(UGen in) {
        this.gainIn.addInput(in);
    }

    @Override
    public void disconnectSource(UGen u) {
        this.gainIn.removeAllConnections(u);
    }

    @Override
    public Gain getOutput() {
        if (this.isEnabled()) {
            return this.gainOut;
        } else {
            throw new IllegalStateException("Cannot produce output.");
        }
    }

    @Override
    public void setVolume(int vol) {
        this.vol.setVolume(vol);
    }

    @Override
    public int getVolume() {
        return this.vol.getVolume();
    }

    public Panner getPanner() {
        return this.pan;
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
    public ProcessingUnit getProcessingUnit() {

    }

    @Override
    public boolean isProcessingUnitPresent() {
        return this.pu.isPresent();
    }

    @Override
    public Type getType() {
        return this.type;
    }

}
