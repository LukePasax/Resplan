package daw.utilities;

import net.beadsproject.beads.core.AudioContext;

/**
 * Singleton based class that allows to get the lone {@link AudioContext} object,
 * which is the link between the Beads library and the Java Sound API.
 */
public final class AudioContextManager {

    private static final AudioContext AC = new AudioContext();

    private AudioContextManager() {}

    public static AudioContext getAudioContext() {
        return AC;
    }

}
