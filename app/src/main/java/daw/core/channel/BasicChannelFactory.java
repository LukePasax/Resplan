package daw.core.channel;

import daw.core.audioprocessing.BasicProcessingUnitBuilder;
import net.beadsproject.beads.core.UGen;
import java.util.Map;

/**
 * Implements a {@link ChannelFactory}.
 */
public class BasicChannelFactory implements ChannelFactory {

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
                .highPassFilter(1, 200.0f)
                .gate(1)
                .build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @param u the audio source that this channel must be sidechained to.
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel sidechained(UGen u) {
        final var bc = new BasicChannel(RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder()
                .sidechain(u, 1)
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
                .reverb(1)
                .highPassFilter(1, 200.0f)
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
        bc.addProcessingUnit(new BasicProcessingUnitBuilder()
                .lowPassFilter(1, 15000.0f)
                .highPassFilter(1, 200.0f)
                .compressor(1)
                .compressor(1)
                .build());
        bc.getProcessingUnit().get()
                .getEffectAtPosition(3)
                .setParameters(Map.of("ratio", Float.POSITIVE_INFINITY));
        return bc;
    }

}
