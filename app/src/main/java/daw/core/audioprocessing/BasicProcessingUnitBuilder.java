package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private Optional<Sidechaining> sidechain;
    private Optional<RPEffect> lowPassFilter;
    private Optional<RPEffect> highPassFilter;
    private Optional<RPEffect> reverb;
    private Optional<RPEffect> gate;
    private Optional<RPEffect> compressor;

    public BasicProcessingUnitBuilder() {
        this.lowPassFilter = Optional.empty();
        this.highPassFilter = Optional.empty();
        this.reverb = Optional.empty();
        this.sidechain = Optional.empty();
        this.gate = Optional.empty();
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
        if (this.lowPassFilter.isEmpty()) {
            this.lowPassFilter = Optional.of(new LowPassFilter(channels, cutoffFrequency));
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder highPassFilter(int channels, float cutoffFrequency) {
        if (this.highPassFilter.isEmpty()) {
            this.highPassFilter = Optional.of(new HighPassFilter(channels, cutoffFrequency));
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder reverb(int channels) {
        if (this.reverb.isEmpty()) {
            this.reverb = Optional.of(new DigitalReverb(channels));
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder gate(int channels) {
        if (this.gate.isEmpty()) {
            this.gate = Optional.of(new Gate(channels));
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder compressor(int channels) {
        if (this.compressor.isEmpty()) {
            this.compressor = Optional.of(new Compression(channels));
        }
        return this;
    }

    @Override
    public ProcessingUnit build() throws IllegalStateException {
        final List<Optional<RPEffect>> effects = new ArrayList<>(List.of(this.lowPassFilter, this.highPassFilter,
                this.compressor, this.gate, this.reverb));
        if (effects.stream().anyMatch(Optional::isPresent)) {
            final var pu = new BasicProcessingUnit(effects.stream().filter(Optional::isPresent).
                    map(Optional::get).collect(Collectors.toList()));
            if (this.sidechain.isPresent()) {
                pu.addSidechaining(this.sidechain.get());
            }
            return pu;
        }
        throw new IllegalStateException();
    }

}
