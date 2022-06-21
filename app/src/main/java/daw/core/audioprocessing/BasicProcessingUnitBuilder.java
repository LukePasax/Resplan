package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents an implementation of {@link ProcessingUnitBuilder} in which an exception upon
 * the call of the method build is raised only if no effect has been specified.
 */
public final class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private boolean sidechainingPresent;
    private final List<RPEffect> effects;

    public BasicProcessingUnitBuilder() {
        this.sidechainingPresent = false;
        this.effects = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * @param u the {@link UGen} which this {@link ProcessingUnit} is sidechained to.
     * @param channels the number of input and output channels for the underlying compressor.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder sidechain(final UGen u, final int channels) {
        if (!this.sidechainingPresent) {
            this.effects.add(new SidechainingImpl(u, channels));
            this.sidechainingPresent = true;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder lowPassFilter(final int channels) {
        this.effects.add(new LowPassFilter(channels));
        return this;
    }

    /**
     * {@inheritDoc}
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder highPassFilter(final int channels) {
        this.effects.add(new HighPassFilter(channels));
        return this;
    }

    /**
     * {@inheritDoc}
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder reverb(final int channels) {
        this.effects.add(new DigitalReverb(channels));
        return this;
    }

    /**
     * {@inheritDoc}
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder gate(final int channels) {
        this.effects.add(new Gate(channels));
        return this;
    }

    /**
     * {@inheritDoc}
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder compressor(final int channels) {
        this.effects.add(new Compression(channels));
        return this;
    }

    /**
     * {@inheritDoc}
     * @param channels the number of input and output channels for this effect.
     * @return a reference to this object.
     */
    @Override
    public ProcessingUnitBuilder limiter(final int channels) {
        this.effects.add(new Limiter(channels));
        return this;
    }

    /**
     * {@inheritDoc}
     * @return a {@link ProcessingUnit} with the ordered sequence of effects.
     * @throws IllegalStateException if no {@link AudioElement} has been added through this builder.
     */
    @Override
    public ProcessingUnit build() throws IllegalStateException {
        if (this.effects.isEmpty()) {
            throw new IllegalStateException("Cannot create an empty processing unit.");
        }
        return new BasicProcessingUnit(this.effects);
    }

}
