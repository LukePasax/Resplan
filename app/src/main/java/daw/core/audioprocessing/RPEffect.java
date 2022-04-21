package daw.core.audioprocessing;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 * This class is an extension of {@link UGen}. In the context of this software, this class is the one
 * all effects must extend. Non-abstract subclasses must provide an implementation for method calculateBuffer.
 */
public abstract class RPEffect extends UGen {

    public RPEffect(AudioContext context, int ins, int outs) {
        super(context, ins, outs);
    }

    @Override
    public abstract void calculateBuffer();

}
