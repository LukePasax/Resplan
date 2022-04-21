package daw;

import Resplan.AudioContextManager;
import daw.core.audioprocessing.BasicProcessingUnit;
import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitInitialization {

    private final TestReflection ref = new TestReflection();

    @Test
    public void testCorrectInitialization() {
        final var pan = new Panner(AudioContextManager.getAudioContext());
        final var comp = new Compressor(AudioContextManager.getAudioContext(), pan.getOuts());
        final var plug = new Plug(AudioContextManager.getAudioContext(), comp, 1);
        final var rev = new Reverb(AudioContextManager.getAudioContext(), plug.getOuts());
        // initializing with multiple UGens
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(pan, comp, plug, rev));
        assertEquals(List.of(Panner.class, Compressor.class, Plug.class, Reverb.class),
                this.ref.getList(pu.getEffects()));
        assertEquals(Set.of(Compressor.class), this.ref.getSet(pu.getEffectAtPosition(2).getConnectedInputs()));
        assertEquals(Set.of(Plug.class), this.ref.getSet(pu.getEffectAtPosition(3).getConnectedInputs()));
    }

    @Test
    public void testIncorrectInitialization() {
        // initializing without specifying any UGen
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
