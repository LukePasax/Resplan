package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;

import java.util.*;
import java.util.stream.Collectors;

public class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private Optional<RPEffect> lowPassFilter;
    private Optional<RPEffect> highPassFilter;
    private Optional<RPEffect> reverb;
    private Optional<CompressorWithSidechaining> sidechain;
    private Optional<RPEffect> gate;
    private Optional<RPEffect> compressor;
    private Optional<RPEffect> limiter;

    public BasicProcessingUnitBuilder() {
        this.lowPassFilter = Optional.empty();
        this.highPassFilter = Optional.empty();
        this.reverb = Optional.empty();
        this.sidechain = Optional.empty();
        this.gate = Optional.empty();
    }

    @Override
    public ProcessingUnitBuilder lowPassFilter() {
        // TO DO
        return this;
    }

    @Override
    public ProcessingUnitBuilder highPassFilter() {
        // TO DO
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
    public ProcessingUnitBuilder sidechain(UGen u, int channels) {
        if (this.sidechain.isEmpty()) {
            this.sidechain = Optional.of(new CompressorWithSidechaining(channels));
        }
        this.sidechain.get().connectSidechain(u);
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
            this.compressor = Optional.of(new CompressorWithSidechaining(channels));
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder limiter(int channels) {
        if (this.compressor.isEmpty()) {
            this.compressor = Optional.of(new Limiter(channels));
        }
        return this;
    }

    @Override
    public ProcessingUnit build() throws IllegalStateException {
        final List<RPEffect> effects = new ArrayList<>(List.of(this.gate.orElse(null),
                this.sidechain.orElse(null), this.compressor.orElse(null),
                this.highPassFilter.orElse(null), this.lowPassFilter.orElse(null), this.reverb.orElse(null)));
        if (effects.stream().anyMatch(Objects::nonNull)) {
            return new BasicProcessingUnit(effects.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        }
        throw new IllegalStateException();
    }

}
