package daw.core.channel;

import daw.general.Volume;
import net.beadsproject.beads.ugens.Gain;

public class BasicChannel implements RPChannel {

    private final Volume vol;
    private final Type type;
    private Gain inputGain;
    private boolean enabled;

    public BasicChannel(final Volume vol, final Type type) {
        this.vol = vol;
        this.type = type;
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
