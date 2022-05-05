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

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel basic() {
        return new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.AUDIO);
    }

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel gated() {
        // TODO: make ProcessingUnit have correct effects
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().gate(1).build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @param u the audio source that this channel must be sidechained to.
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel sidechained(UGen u) {
        // TODO: make ProcessingUnit have correct effects
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.AUDIO);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().sidechain(u, 1)
                .highPassFilter(1,100.0f).build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel returnChannel() {
        // TODO: make ProcessingUnit have correct effects
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.RETURN);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().highPassFilter(1, 100.0f).build());
        return bc;
    }

    /**
     * {@inheritDoc}
     * @return the {@link RPChannel}.
     */
    @Override
    public RPChannel masterChannel() {
        // TODO: make ProcessingUnit have correct effects
        final var bc = new BasicChannel(new BasicVolume(DEFAULT_VOLUME, DEFAULT_MAX_VOLUME),
                new Panner(), RPChannel.Type.MASTER);
        bc.addProcessingUnit(new BasicProcessingUnitBuilder().compressor(1).build());
        return bc;
    }

}
