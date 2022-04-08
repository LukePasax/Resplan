package daw.core.channel;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicProcessingUnitBuilder implements ProcessingUnitBuilder {

    private Optional<UGen> filter;
    private Optional<UGen> delay;
    private Optional<UGen> compressor;
    private Optional<UGen> reverb;
    private Optional<UGen> sidechain;
    private Optional<UGen> gate;

    @Override
    public ProcessingUnitBuilder filter() {
        this.filter = Optional.ofNullable(new CrossoverFilter());
        return this;
    }

    @Override
    public ProcessingUnitBuilder delay() {
        this.filter = Optional.ofNullable(null);
        return this;
    }

    @Override
    public ProcessingUnitBuilder compressor() {
        this.compressor = Optional.of(new Compressor());
        return this;
    }

    @Override
    public ProcessingUnitBuilder reverb() {
        this.reverb = Optional.of(new Reverb());
        return this;
    }

    @Override
    public ProcessingUnitBuilder sidechain() {
        return null;
    }

    @Override
    public ProcessingUnitBuilder gate() {
        return null;
    }

    @Override
    public ProcessingUnit build() throws IllegalStateException {
        final List<Optional<UGen>> list = new ArrayList<>(List.of(this.filter, this.delay, this.compressor,
                this.gate, this.reverb, this.sidechain));
        if (list.stream().filter(x -> x.isPresent()).count() != 0) {
            return new BasicProcessingUnit(list.stream().map(x -> x.get()).collect(Collectors.toList()));
        } else {
            throw new IllegalStateException();
        }
    }
}
