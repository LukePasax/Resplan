package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;
import java.util.*;

public class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private Optional<Sidechaining> sidechain;
    private final List<RPEffect> effects;

    public BasicProcessingUnitBuilder() {
        this.sidechain = Optional.empty();
        this.effects = new ArrayList<>();
    }

    @Override
    public ProcessingUnitBuilder sidechain(UGen u, int channels) {
        if (this.sidechain.isEmpty()) {
            this.sidechain = Optional.of(new Sidechaining(u, channels));
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder lowPassFilter(int channels, float cutoffFrequency) {
        this.effects.add(new LowPassFilter(channels, cutoffFrequency));
        return this;
    }

    @Override
    public ProcessingUnitBuilder highPassFilter(int channels, float cutoffFrequency) {
        this.effects.add(new HighPassFilter(channels, cutoffFrequency));
        return this;
    }

    @Override
    public ProcessingUnitBuilder reverb(int channels) {
        this.effects.add(new DigitalReverb(channels));
        return this;
    }

    @Override
    public ProcessingUnitBuilder gate(int channels) {
        this.effects.add(new Gate(channels));
        return this;
    }

    @Override
    public ProcessingUnitBuilder compressor(int channels) {
        this.effects.add(new Compression(channels));
        return this;
    }

    @Override
    public ProcessingUnit build() throws IllegalStateException {
        if (!this.effects.isEmpty()) {
            final var pu = new BasicProcessingUnit(effects);
            this.sidechain.ifPresent(pu::addSidechaining);
            return pu;
        }
        throw new IllegalStateException("Cannot create an empty ProcessingUnit.");
    }

}
