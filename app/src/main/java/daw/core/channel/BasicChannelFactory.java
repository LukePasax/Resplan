package daw.core.channel;

import daw.core.audioprocessing.BasicProcessingUnitBuilder;
import daw.general.BasicVolume;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Panner;

/**
 * Implements a {@link ChannelFactory}.
 */
public class BasicChannelFactory implements ChannelFactory {

    private static final int DEFAULT_VOLUME = 40;
    private static final int DEFAULT_MAX_VOLUME = 100;

    @Override
    public RPChannel basic() {
        return new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.AUDIO);
    }

    @Override
    public RPChannel gated() {
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().gate(1).build());
        return bc;
    }

    @Override
    public RPChannel sidechained(UGen u) {
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().sidechain(u, 1).build());
        return bc;
    }

    @Override
    public RPChannel returnChannel() {
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.RETURN);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().build());
        return bc;
    }

    @Override
    public RPChannel masterChannel() {
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.MASTER);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().build());
        return bc;
    }
}
