package daw.core.channel;

import daw.core.effect.Sidechain;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private Optional<UGen> filter;
    private Optional<UGen> compressor;
    private Optional<UGen> reverb;
    private Optional<UGen> sidechain;
    private Optional<UGen> gate;

    public BasicProcessingUnitBuilder() {
        this.filter = Optional.empty();
        this.compressor = Optional.empty();
        this.reverb = Optional.empty();
        this.sidechain = Optional.empty();
        this.gate = Optional.empty();
    }

    @Override
    public ProcessingUnitBuilder filter() {
        if (this.filter.isEmpty()) {
            this.filter = Optional.of(new CrossoverFilter());
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder compressor() {
        if (this.compressor.isEmpty()) {
            this.compressor = Optional.of(new Compressor());
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder reverb() {
        if (this.reverb.isEmpty()) {
            this.reverb = Optional.of(new Reverb());
        }
        return this;
    }

    @Override
    public ProcessingUnitBuilder sidechain() {
        // TO DO: make Sidechain instantiable
        this.sidechain = Optional.empty();
        return this;
    }

    @Override
    public ProcessingUnitBuilder gate() {
        // TO DO: make Gate instantiable
        this.gate = Optional.empty();
        return null;
    }

    @Override
    public ProcessingUnit build() throws IllegalStateException {
        final List<Optional<UGen>> effects = new ArrayList<>(List.of(
                this.filter, this.compressor, this.gate, this.reverb, this.sidechain));
        if (effects.stream().filter(Optional::isPresent).count() != 0) {
            return new BasicProcessingUnit(effects.stream().filter(Optional::isPresent).map(Optional::get)
                    .collect(Collectors.toList()));
        }
        throw new IllegalStateException();
    }

}
