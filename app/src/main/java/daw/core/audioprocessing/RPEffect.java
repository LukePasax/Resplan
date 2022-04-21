package daw.core.audioprocessing;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBeadReceiver;

public abstract class RPEffect extends UGen {

    public RPEffect(AudioContext context, int ins, int outs) {
        super(context, ins, outs);
    }

    @Override
    public abstract void calculateBuffer();

}
