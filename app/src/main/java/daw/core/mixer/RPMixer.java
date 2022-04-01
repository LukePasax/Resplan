package daw.core.mixer;

import daw.core.channel.RPChannel;
import daw.core.clip.RPClip;
import net.beadsproject.beads.core.AudioContext;

import java.util.List;

public interface RPMixer {

    void setAudioContext(AudioContext context);

    AudioContext getAudioContext();

    List<RPChannel> getChannels();

    void createChannel();

    RPChannel getChannel(RPClip clip);

    // wait for Return Channel interface
    void linkChannel(RPChannel channel);

    void createReturnChannel();


}
