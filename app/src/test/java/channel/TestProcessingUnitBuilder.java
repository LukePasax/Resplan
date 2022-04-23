package channel;

import daw.core.audioprocessing.BasicProcessingUnitBuilder;
import daw.core.audioprocessing.ProcessingUnit;
import daw.core.audioprocessing.Gate;
import daw.core.audioprocessing.Sidechain;
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
            final ProcessingUnit pu = builder.reverb().build();
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
            final ProcessingUnit pu = builder.gate().reverb().build();
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
