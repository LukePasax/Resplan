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

    /**
     * Allows getting the value associated to each parameter of this particular effect.
     * Parameters influence how the sound is processed by this effect.
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    public abstract Map<String, Float> getParameters();

    /**
     * Modifies the value of all the parameters specified as keys in the given map. Those parameters
     * will then contain the value associated to them in the map.
     * The keys that do not match any of the effect's parameters are ignored
     * and so are the values associated to them.
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    public abstract void setParameters(Map<String, Float> parameters);

    /**
     * Allows getting the default value of a certain parameter of this particular effect.
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    protected abstract float getDefaultValue(String key);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void calculateBuffer();

}
