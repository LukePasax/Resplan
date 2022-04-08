package daw;

import daw.core.channel.BasicProcessingUnitBuilder;
import daw.core.channel.ProcessingUnit;
import daw.core.effect.Gate;
import daw.core.effect.Sidechain;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.CrossoverFilter;
import net.beadsproject.beads.ugens.Reverb;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProcessingUnitBuilder {

    @Test
    public void testCorrectBuild() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.filter().reverb().build();
            assertEquals(List.of(CrossoverFilter.class, Reverb.class),
                    pu.getEffects().stream().map(Object::getClass).collect(Collectors.toList()));
        } catch (IllegalStateException e) {
            fail("Builder should have succeeded");
        }
    }

    @Test
    public void testCorrectBuildComplete() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.gate().sidechain().reverb().compressor().filter().build();
            assertEquals(List.of(Gate.class, Sidechain.class, Reverb.class, Compressor.class, CrossoverFilter.class),
                    pu.getEffects().stream().map(Object::getClass).collect(Collectors.toList()));
        } catch (IllegalStateException e) {
            fail("Builder should have succeeded");
        }
    }

    @Test
    public void testWrongBuild() {
        final var builder = new BasicProcessingUnitBuilder();
        final ProcessingUnit pu = builder.build();
        fail();
    }

}
