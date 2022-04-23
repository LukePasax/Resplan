package channel;

import Resplan.AudioContextManager;
import daw.core.audioprocessing.BasicProcessingUnitBuilder;
import daw.core.audioprocessing.ProcessingUnit;
import daw.core.audioprocessing.Gate;
import net.beadsproject.beads.ugens.Compressor;
import net.beadsproject.beads.ugens.Reverb;
import net.beadsproject.beads.ugens.SamplePlayer;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitBuilder {

    private final TestReflection ref = new TestReflection();

    @Test
    public void testCorrectBuild() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.reverb(1).sidechain(
                    new SamplePlayer(AudioContextManager.getAudioContext(),1),1).build();
            assertEquals(List.of(Reverb.class), this.ref.getList(pu.getEffects()));
            assertTrue(pu.isSidechainingPresent());
        } catch (IllegalStateException e) {
            fail("Builder should have succeeded");
        }
    }

    @Test
    public void testCorrectBuildComplete() {
        final var builder = new BasicProcessingUnitBuilder();
        try {
            final ProcessingUnit pu = builder.gate(1).reverb(1).compressor(1).build();
            assertEquals(List.of(Gate.class, Reverb.class, Compressor.class),
                    this.ref.getList(pu.getEffects()));
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
