package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private Optional<RPEffect> lowPassFilter;
    private Optional<RPEffect> highPassFilter;
    private Optional<RPEffect> reverb;
    private Optional<RPEffect> sidechain;
    private Optional<RPEffect> gate;

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
    public ProcessingUnitBuilder reverb() {
        // TO DO
        return this;
    }

    @Override
    public ProcessingUnitBuilder sidechain(UGen u) {
        // TO DO
        return this;
    }

    @Override
    public ProcessingUnitBuilder gate() {
        // TO DO
        return this;
    }

    @Override
    public ProcessingUnit build() throws IllegalStateException {
        final List<Optional<RPEffect>> effects = new ArrayList<>(List.of(
                this.lowPassFilter, this.highPassFilter, this.gate, this.reverb, this.sidechain));
        if (effects.stream().filter(Optional::isPresent).count() != 0) {
            return new BasicProcessingUnit(effects.stream().filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList()));
        }
        throw new IllegalStateException();
    }

}
