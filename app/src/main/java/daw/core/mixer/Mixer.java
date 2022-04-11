package daw.core.mixer;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.ChannelFactory;
import daw.core.channel.RPChannel;
import daw.core.clip.RPClip;

import java.util.*;
import java.util.stream.Collectors;

public class Mixer implements RPMixer{

    private Map<RPChannel, List<RPClip>> channelMap;
    private ChannelFactory channelFactory;
    private RPChannel masterChannel;

    Mixer() {
        channelMap = new HashMap<>();
        channelFactory = new BasicChannelFactory();
        masterChannel = channelFactory.masterChannel();
    }

    @Override
    public List<RPChannel> getChannels() {
        return new ArrayList<>(channelMap.keySet());
    }

    @Override
    public void createChannel(RPChannel.Type type) {
        if (type == RPChannel.Type.GATED) {
            channelMap.put(channelFactory.gated(),new ArrayList<>());
        } else if (type == RPChannel.Type.RETURN) {
            channelMap.put(channelFactory.returnChannel(),new ArrayList<>());
        } else if (type == RPChannel.Type.SIDECHAINED) {
            channelMap.put(channelFactory.sidechained(),new ArrayList<>());
        }
    }

    @Override
    public RPChannel getChannel(RPClip clip) {
        return channelMap.entrySet().stream().filter(e -> e.getValue().contains(clip)).map(Map.Entry::getKey).findAny().get();
    }

    @Override
    public RPChannel getMasterChannel() {
        return this.masterChannel;
    }

    @Override
    public void linkChannel(RPChannel channel, RPChannel returnChannel) throws IllegalArgumentException {
        if (returnChannel.getType() != RPChannel.Type.RETURN) {
            throw new IllegalArgumentException();
        }
    }
}
