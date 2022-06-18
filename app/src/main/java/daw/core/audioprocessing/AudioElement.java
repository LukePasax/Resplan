package daw.core.audioprocessing;

import net.beadsproject.beads.ugens.Gain;
import java.util.Map;

/**
 * Interface that models an element that can be used to connect audio sources. Specifically, an implementation
 * of this interface must have one input and one output {@link Gain}. It is important to underline the fact that
 * an audio element does not necessarily have to process audio, as it may just connect two other audio element or
 * sources. Still, the interface has methods specifically thought to set and obtain the element parameters' values.
 * This does mean that any audio effect implementation can be a subclass of this interface.
 * The difference in how the sound is manipulated by the effect is going to be reflected by its parameters.
 */
public interface AudioElement {

    /**
     * Allows getting the value associated to each parameter of this particular element.
     * Parameters influence how the sound is processed by this element.
     * @return a {@link Map} where the keys are the parameters and the values are the
     * current value of each parameter of the element.
     */
    Map<String, Float> getParameters();

    /**
     * Modifies the value of all the parameters specified as keys in the given map. Those parameters
     * will then contain the value associated to them in the map.
     * The keys that do not match any of the element's parameters are ignored
     * and so are the values associated to them.
     * @param parameters the {@link Map} that contains the parameters that must be modified.
     */
    void setParameters(Map<String, Float> parameters);

    /**
     * Allows to get the number of input channels of the element.
     * @return an integer that represents the number of input.
     */
    int getIns();

    /**
     * Allows to get the number of output channels of the element.
     * @return an integer that represents the number of outputs.
     */
    int getOuts();

    /**
     * Gets the input of the audio element.
     * @return the {@link Gain} that represents the audio before the processing.
     */
    Gain getGainIn();

    /**
     * Gets the output of the audio element.
     * @return the {@link Gain} that represents the audio processed by the element.
     */
    Gain getGainOut();

}
