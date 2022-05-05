package daw.core.audioprocessing;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import java.util.Map;

/**
 * This class is an extension of {@link UGen}. In the context of this software, this class is the one
 * all effects must extend. Non-abstract subclasses must provide an implementation for method calculateBuffer.
 */
public abstract class RPEffect extends UGen {

    /**
     * Base constructor for all the effects of this software.
     * @param context the {@link AudioContext} used by this effect.
     * @param ins the number of inputs of this effect.
     * @param outs the number of output of this effect.
     */
    protected RPEffect(AudioContext context, int ins, int outs) {
        super(context, ins, outs);
    }

    public abstract Map<String, Float> getParameters();

    public abstract void setParameters(Map<String, Float> parameters);

    protected abstract float getDefaultValue(String key);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateBuffer();

}
