package daw.core.channel;

import net.beadsproject.beads.ugens.Gain;

/**
 * This interface models a channel, that is a...
 */
public interface RSChannel {

    void addInput(Gain g);

    void setVolume(int vol);

    int getVolume();

    void enable();

    void disable();

    boolean isEnabled();
}
