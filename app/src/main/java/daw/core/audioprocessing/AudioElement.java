package daw.core.audioprocessing;

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
     * Allows getting the default value of a certain parameter of this particular effect.
     * @param key a parameter of this effect.
     * @return the floating-point default value of the parameter.
     * @throws IllegalArgumentException if the given string does not match any of the parameters of this effect.
     */
    float getDefaultValue(String key);

    /**
     * Sets a default value for the parameter specified by the given key. If there is no such parameter,
     * this method does nothing.
     * @param key the name of a parameter.
     * @param value the value that the key has as default after this method is called.
     */
    void setDefaultValue(String key, float value);

}