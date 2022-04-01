package daw.core.channel;

import net.beadsproject.beads.ugens.Gain;

public class MasterChannel implements RPChannel {

    @Override
    public void addInput(Gain g) {

    }

    @Override
    public void setVolume(int vol) {

    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
