package channel;

import daw.utilities.AudioContextManager;
import daw.core.audioprocessing.*;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Reverb;
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
            final ProcessingUnit pu = builder.compressor(1).highPassFilter(2,100.0f)
                    .reverb(1).lowPassFilter(1,100.0f).sidechain(
                    new SamplePlayer(AudioContextManager.getAudioContext(),1),1).build();
            assertEquals(List.of(Compression.class, HighPassFilter.class, Reverb.class, LowPassFilter.class),
                    this.ref.getList(pu.getEffects()));
            assertTrue(pu.isSidechainingPresent());
            assertEquals(Set.of(HighPassFilter.class),
                    this.ref.getSet(pu.getEffectAtPosition(2).getConnectedInputs()));
            assertEquals(Set.of(DigitalReverb.class),
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
            assertEquals(List.of(Gate.class, Reverb.class, Compressor.class),
                    this.ref.getList(pu.getEffects()));
            assertEquals(Set.of(), this.ref.getSet(pu.getEffectAtPosition(0).getConnectedInputs()));
            assertFalse(pu.isSidechainingPresent());
        } catch (IllegalStateException ex) {
            fail("Builder should have succeeded");
        }
    }

    @Test
    public void testWrongBuild() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.build();
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

}
