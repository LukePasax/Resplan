package daw.core.mixer;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.ChannelFactory;
import daw.core.channel.RPChannel;
import net.beadsproject.beads.ugens.Gain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mixer implements RPMixer{

    private final ChannelFactory channelFactory;
    private final RPChannel masterChannel;

    public Mixer() {
        channelFactory = new BasicChannelFactory();
        masterChannel = channelFactory.masterChannel();
    }

    @Override
    public RPChannel createChannel(RPChannel.Type type, Optional<Gain> gain) {
        RPChannel channel = null;
        if (type == RPChannel.Type.GATED) {
            channel = channelFactory.gated();
        } else if (type == RPChannel.Type.RETURN) {
            channel = channelFactory.returnChannel();
        } else if (type == RPChannel.Type.SIDECHAINED) {
            channel = channelFactory.sidechained(gain.get());
        }
        masterChannel.addInput(channel.getOutput());
        return channel;
    }

    @Override
    public RPChannel getMasterChannel() {
        return this.masterChannel;
    }

    @Override
    public void linkChannel(RPChannel channel, RPChannel returnChannel) throws IllegalArgumentException {
        if (returnChannel.getType() != RPChannel.Type.RETURN) {
            throw new IllegalArgumentException();
        } else {
            returnChannel.addInput(channel.getOutput());
        }
    }

    /**
     * A method to add a {@link RPChannel} to a group
     *
     * @param channel the {@link RPChannel} to be added
     * @param group  the Group
     */
    @Override
    public void linkToGroup(RPChannel channel, RPChannel group) {
        group.addInput(channel.getOutput());
    }
}
