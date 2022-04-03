package daw.core.channel;

import daw.general.BasicVolume;

/**
 * Implements a {@link ChannelFactory}.
 */
public class BasicChannelFactory implements ChannelFactory {

    @Override
    public RPChannel basic() {
        return new BasicChannel(new BasicVolume(10, 100), RPChannel.Type.AUDIO, null);
    }

    @Override
    public RPChannel gated() {
        // TO DO: initialize the ProcessingUnit via Builder
        return new BasicChannel(new BasicVolume(10, 100), RPChannel.Type.AUDIO, null);
    }

    @Override
    public RPChannel sidechained() {
        // TO DO: initialize the ProcessingUnit via Builder
        return new BasicChannel(new BasicVolume(10, 100), RPChannel.Type.AUDIO, null);
    }

    @Override
    public RPChannel returnChannel() {
        // TO DO: initialize the ProcessingUnit via Builder
        return new BasicChannel(new BasicVolume(10, 100), RPChannel.Type.RETURN, null);
    }

    @Override
    public RPChannel masterChannel() {
        // TO DO: initialize the ProcessingUnit via Builder
        return new BasicChannel(new BasicVolume(10, 100), RPChannel.Type.MASTER, null);
    }
}
