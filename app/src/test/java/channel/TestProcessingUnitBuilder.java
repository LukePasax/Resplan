package channel;

import daw.utilities.AudioContextManager;
import daw.core.audioprocessing.*;
import net.beadsproject.beads.ugens.SamplePlayer;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitBuilder {

    private final TestReflection ref = new TestReflection();

    @Test
    public void testCorrectBuildWithSidechaining() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.compressor(1).highPassFilter(2)
                    .reverb(1).lowPassFilter(1).sidechain(
                    new SamplePlayer(AudioContextManager.getAudioContext(),1),1).build();
            assertEquals(List.of(SidechainingImpl.class, Compression.class, HighPassFilter.class,
                            DigitalReverb.class, LowPassFilter.class), this.ref.getList(pu.getEffects()));
            assertTrue(pu.isSidechainingPresent());
            assertEquals(Set.of(SidechainingImpl.class),
                    this.ref.getSet(pu.getEffectAtPosition(1).getConnectedInputs()));
            assertEquals(Set.of(HighPassFilter.class),
                    this.ref.getSet(pu.getEffectAtPosition(3).getConnectedInputs()));
        } catch (IllegalStateException e) {
            fail("Builder should have succeeded");
        }
    }

    @Test
    public void testCorrectBuildWithoutSidechaining() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.gate(1).reverb(1).compressor(1).build();
            assertEquals(List.of(Gate.class, DigitalReverb.class, Compression.class),
                    this.ref.getList(pu.getEffects()));
            assertEquals(Set.of(), this.ref.getSet(pu.getEffectAtPosition(0).getConnectedInputs()));
            assertFalse(pu.isSidechainingPresent());
        } catch (IllegalStateException ex) {
            fail("Builder should have succeeded");
        }
    }

    @Test
    public void testWrongBuild() {
        try {
            new BasicProcessingUnitBuilder().build();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

}
