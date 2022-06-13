package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.Map;

/**
 *
 */
public interface AudioElement {

    /**
     * Allows getting the value associated to each parameter of this particular effect.
     * Parameters influence how the sound is processed by this effect.
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the effect.
     */
    Map<String, Float> getParameters();

    /**
     * Modifies the value of all the parameters specified as keys in the given map. Those parameters
     * will then contain the value associated to them in the map.
     * The keys that do not match any of the effect's parameters are ignored
     * and so are the values associated to them.
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    void setParameters(Map<String, Float> parameters);

    /**
     * Allows to get the number of input channels of the effect.
     * @return an integer that represents the number of input.
     */
    int getIns();

    /**
     * Allows to get the number of output channels of the effect.
     * @return an integer that represents the number of outputs.
     */
    int getOuts();

    /**
     * Gets the output of the audio element.
     * @return the {@link Gain} that represents the audio processed by the element.
     */
    Gain getOutput();

}
