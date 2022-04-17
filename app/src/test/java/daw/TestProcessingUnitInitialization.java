package daw;

import daw.core.channel.BasicProcessingUnit;
import daw.core.channel.ProcessingUnit;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitInitialization {

    private TestReflection ref = new TestReflection();

    @Test
    public void testInitialization() {
        // initializing with multiple UGens
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Panner(), new Compressor(),
                new CrossoverFilter(), new Reverb()));
        assertEquals(List.of(Panner.class, Compressor.class, CrossoverFilter.class, Reverb.class),
                this.ref.getList(pu.getEffects()));
        assertEquals(Set.of(Compressor.class), pu.getEffectAtPosition(2)
                .getConnectedInputs().stream().map(Object::getClass).collect(Collectors.toSet()));
        // initializing with no UGens
        try {
            new BasicProcessingUnit(List.of());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        // initializing with no List
        try {
            new BasicProcessingUnit(null);
            fail();
        } catch(IllegalArgumentException ignored) {
        }
    }
}
