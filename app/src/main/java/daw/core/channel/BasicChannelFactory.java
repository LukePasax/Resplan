package daw.core.channel;

import daw.general.BasicVolume;
import net.beadsproject.beads.ugens.Panner;

/**
 * Implements a {@link ChannelFactory}.
 */
// TO DO: these are just random examples of ProcessingUnits for testing purposes.
public class BasicChannelFactory implements ChannelFactory {

    @Override
    public RPChannel basic() {
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.AUDIO, null);
    }

    @Override
    public RPChannel gated() {
        // TO DO: make a Gate class, as there isn't in Beads.
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.AUDIO,
                new BasicProcessingUnitBuilder().gate().build());
    }

    @Override
    public RPChannel sidechained() {
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.AUDIO,
                new BasicProcessingUnitBuilder().sidechain().build());
    }

    @Override
    public RPChannel returnChannel() {
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.RETURN,
                new BasicProcessingUnitBuilder().delay().filter().build());
    }

    @Override
    public RPChannel masterChannel() {
        // TO DO: initialize the ProcessingUnit via Builder
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.MASTER,
                new BasicProcessingUnitBuilder().reverb().compressor().build());
    }
}
