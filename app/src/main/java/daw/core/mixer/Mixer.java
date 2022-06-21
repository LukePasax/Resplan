package daw.core.mixer;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.ChannelFactory;
import daw.core.channel.RPChannel;
import daw.utilities.AudioContextManager;

public class Mixer implements RPMixer {

    private final ChannelFactory channelFactory;
    private final RPChannel masterChannel;

    public Mixer() {
        this.channelFactory = new BasicChannelFactory();
        this.masterChannel = this.channelFactory.masterChannel();
    }

    /**
     * A method to create a Basic {@link RPChannel} in the mixer
     *
     * @return the {@link RPChannel} that is created
     */
    @Override
    public RPChannel createBasicChannel() {
        final RPChannel channel = this.channelFactory.basic();
        this.linkToMaster(channel);
        return channel;
    }

    /**
     * A method to create a Gated {@link RPChannel} in the mixer
     *
     * @return the {@link RPChannel} that is created
     */
    @Override
    public RPChannel createGatedChannel() {
        final RPChannel channel = this.channelFactory.gated();
        this.linkToMaster(channel);
        return channel;
    }

    /**
     * A method to create a Return {@link RPChannel} in the mixer
     *
     * @return the {@link RPChannel} that is created
     */
    @Override
    public RPChannel createReturnChannel() {
        final RPChannel channel = this.channelFactory.returnChannel();
        this.linkToMaster(channel);
        return channel;
    }

    private void linkToMaster( final RPChannel channel) {
        this.masterChannel.connectSource(channel.getOutput());
    }

    /**
     * This method is used to create a sidechained {@link RPChannel}
     *
     * @param channel the {@link RPChannel} to sidechain
     * @return the sidechained {@link RPChannel}
     */
    @Override
    public RPChannel createSidechained( final RPChannel channel) {
        final RPChannel sidechained = this.channelFactory.sidechained(channel.getOutput());
        this.linkToMaster(sidechained);
        return sidechained;
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
    public void linkChannel( final RPChannel channel, final RPChannel returnChannel) throws IllegalArgumentException {
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
    public void linkToGroup( final RPChannel channel, final RPChannel group) {
        this.masterChannel.disconnectSource(channel.getOutput());
        group.connectSource(channel.getOutput());
    }

    /**
     * A method to remove a {@link RPChannel} from a group
     *
     * @param channel the {@link RPChannel} to be removed
     * @param group   the group to remove the {@link RPChannel} from
     */
    @Override
    public void unlinkFromGroup( final RPChannel channel, final RPChannel group) {
        group.disconnectSource(channel.getOutput());
        this.masterChannel.connectSource(channel.getOutput());
    }

    /**
     * A method to link a sidechained {@link RPChannel}
     *
     * @param channel            the {@link RPChannel} to sidechain
     * @param sidechainedChannel the sidechained {@link RPChannel}
     */
    @Override
    public void linkToSidechained( final RPChannel channel, final RPChannel sidechainedChannel) {
    }

    @Override
    public void connectToSystem() {
        AudioContextManager.getAudioContext().out.addInput(this.masterChannel.getOutput());
    }

}
