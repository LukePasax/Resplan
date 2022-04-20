package daw.core.mixer;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.ChannelFactory;
import daw.core.channel.RPChannel;

public class Mixer implements RPMixer{

    private final ChannelFactory channelFactory;
    private final RPChannel masterChannel;

    public Mixer() {
        channelFactory = new BasicChannelFactory();
        masterChannel = channelFactory.masterChannel();
    }

    /**
     * A method to create a {@link RPChannel} in the mixer
     * and links its output to the Master channel.
     * @param type the {@link RPChannel} type that will be created
     * @return the {@link RPChannel} that is created
     */
    @Override
    public RPChannel createChannel(RPChannel.Type type) {
        RPChannel channel = null;
        if (type == RPChannel.Type.GATED) {
            channel = channelFactory.gated();
        } else if (type == RPChannel.Type.RETURN) {
            channel = channelFactory.returnChannel();
        }
        masterChannel.connectSource(channel.getOutput());
        return channel;
    }

    /**
     * This method is used to create a sidechained {@link RPChannel}
     *
     * @param channel the {@link RPChannel} to sidechain
     * @return the sidechained {@link RPChannel}
     */
    @Override
    public RPChannel createSidechained(RPChannel channel) {
        return null;
    }

    /**
     * A method that returns the Master channel of the mixer.
     * @return the Master channel contained in the mixer
     */
    @Override
    public RPChannel getMasterChannel() {
        return this.masterChannel;
    }

    /**
     * A method to link the output of a {@link RPChannel}
     * to the Input of a Return channel.
     * @param channel the {@link RPChannel} which output is to be linked
     * @param returnChannel the Return channel which receives the input
     */
    @Override
    public void linkChannel(RPChannel channel, RPChannel returnChannel) throws IllegalArgumentException {
        if (returnChannel.getType() != RPChannel.Type.RETURN) {
            throw new IllegalArgumentException();
        } else {
            returnChannel.connectSource(channel.getOutput());
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
        masterChannel.disconnectSource(channel.getOutput());
        group.connectSource(channel.getOutput());
    }

    /**
     * A method to remove a {@link RPChannel} from a group
     *
     * @param channel the {@link RPChannel} to remove
     * @param group   the group to remove the {@link RPChannel} from
     */
    @Override
    public void removeFromGroup(RPChannel channel, RPChannel group) {
        group.disconnectSource(channel.getOutput());
        masterChannel.connectSource(channel.getOutput());
    }
}
