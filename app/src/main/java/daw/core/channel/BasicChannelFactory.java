package daw.core.channel;

import daw.core.audioprocessing.BasicProcessingUnitBuilder;
import net.beadsproject.beads.core.UGen;

/**
 * Implements a {@link ChannelFactory}.
 */
public class BasicChannelFactory implements ChannelFactory {

    private final static int INS = 2;

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel basic() {
        return new BasicChannel(RPChannel.Type.AUDIO);
    }

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel gated() {
        final var bc = new BasicChannel(RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder()
                .highPassFilter(INS)
                .gate(INS)
                .build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @param u the audio source that this channel must be sidechained to.
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel sidechained(final UGen u) {
        final var bc = new BasicChannel(RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder()
                .sidechain(u, INS)
                .build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel returnChannel() {
        final var bc = new BasicChannel(RPChannel.Type.RETURN);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder()
                .reverb(INS)
                .highPassFilter(INS)
                .build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel masterChannel() {
        final var bc = new BasicChannel(RPChannel.Type.MASTER);
        final var pu = new BasicProcessingUnitBuilder()
                .lowPassFilter(INS)  // 0
                .highPassFilter(INS)   // 1
                .compressor(INS)  // 2
                .limiter(INS)  // 3
                .build();
        bc.addProcessingUnit(pu);
        return bc;
    }

}
