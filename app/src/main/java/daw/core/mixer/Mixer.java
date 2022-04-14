package daw.core.mixer;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.ChannelFactory;
import daw.core.channel.RPChannel;

import java.util.ArrayList;
import java.util.List;

public class Mixer implements RPMixer{

    private final List<RPChannel> channelList;
    private final ChannelFactory channelFactory;
    private final RPChannel masterChannel;

    Mixer() {
        channelList = new ArrayList<>();
        channelFactory = new BasicChannelFactory();
        masterChannel = channelFactory.masterChannel();
    }

    @Override
    public List<RPChannel> getChannels() {
        return this.channelList;
    }

    @Override
    public void createChannel(RPChannel.Type type) {
        if (type == RPChannel.Type.GATED) {
            channelList.add(channelFactory.gated());
        } else if (type == RPChannel.Type.RETURN) {
           channelList.add(channelFactory.returnChannel());
        } else if (type == RPChannel.Type.SIDECHAINED) {
            channelList.add(channelFactory.sidechained());
        }
        //this.masterChannel.addInput(channelList.get(channelList.size()-1));
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
            //returnChannel.addInput();
        }
    }
}
