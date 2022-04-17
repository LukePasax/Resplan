package daw.core.channel;

import daw.general.BasicVolume;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Panner;

/**
 * Implements a {@link ChannelFactory}.
 */
// TO DO: these are just random examples of ProcessingUnits for testing purposes.
public class BasicChannelFactory implements ChannelFactory {

    @Override
    public RPChannel basic() {
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.BASIC, null);
    }

    @Override
    public RPChannel gated() {
        // TO DO: make a Gate class, as there isn't in Beads.
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.GATED,
                new BasicProcessingUnitBuilder().gate().highPassFilter().build());
    }

    @Override
    public RPChannel sidechained(UGen u) {
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.SIDECHAINED,
                new BasicProcessingUnitBuilder().sidechain(u).build());
    }

    @Override
    public RPChannel returnChannel() {
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.RETURN,
                new BasicProcessingUnitBuilder().lowPassFilter().build());
    }

    @Override
    public RPChannel masterChannel() {
        // TO DO: initialize the ProcessingUnit via Builder
        return new BasicChannel(new BasicVolume(10, 100), new Panner(), RPChannel.Type.MASTER,
                new BasicProcessingUnitBuilder().reverb().highPassFilter().build());
    }
}
