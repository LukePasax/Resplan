package daw.core.channel;

import net.beadsproject.beads.ugens.Gain;

/**
 * This interface models a channel, which is a representation of sound coming from an input and going to an output.
 */
public interface RSChannel {

    void addInput(Gain g);

    void setVolume(int vol);

    int getVolume();

    void enable();

    void disable();

    boolean isEnabled();
}
