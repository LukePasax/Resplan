package Resplan;

import net.beadsproject.beads.core.AudioContext;

public class AudioContextManager {
    private static final AudioContext AC = new AudioContext();

    private AudioContextManager(){}

    public static AudioContext getAudioContext(){
        return AC;
    }

}
