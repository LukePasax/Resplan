package channel;

import daw.utilities.AudioContextManager;
import daw.core.audioprocessing.*;
import net.beadsproject.beads.ugens.SamplePlayer;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitBuilder {

    private final TestUtility ref = new TestUtility();

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
            assertTrue(this.ref.effectsAreConnected(pu.getEffectAtPosition(0), pu.getEffectAtPosition(1)));
            assertTrue(this.ref.effectsAreConnected(pu.getEffectAtPosition(2), pu.getEffectAtPosition(3)));
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
