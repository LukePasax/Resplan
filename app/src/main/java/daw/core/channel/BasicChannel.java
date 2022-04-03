package daw.core.channel;

import daw.general.Volume;
import net.beadsproject.beads.ugens.Gain;

public class BasicChannel implements RPChannel {

    private final Volume vol;
    private final Type type;
    private final ProcessingUnit pu;
    private Gain inputGain;
    private boolean enabled;

    private BasicChannel(final Volume vol, final Type type, ProcessingUnit pu) {
        this.vol = vol;
        this.type = type;
        this.pu = pu;
    }

    @Override
    public void addInput(Gain g) {
        this.inputGain = g;
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
}
