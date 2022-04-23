package channel;

import Resplan.AudioContextManager;
import daw.core.audioprocessing.BasicProcessingUnit;
import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProcessingUnitAddition {

    private final TestReflection ref = new TestReflection();
    private final ProcessingUnit pu = new BasicProcessingUnit(
            List.of(new Compressor(AudioContextManager.getAudioContext())));

    @Test
    public void testIllegalUGenAddition() {
        try {
            pu.addEffect(new Glide(AudioContextManager.getAudioContext()));
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testCorrectAddition() {
        // adding at the last position using addEffect
        pu.addEffect(new Plug(AudioContextManager.getAudioContext()));
        assertEquals(List.of(Compressor.class, Plug.class), this.ref.getList(pu.getEffects()));
        // adding at a given position
        pu.addEffectAtPosition(new Maximum(AudioContextManager.getAudioContext()), 1);
        assertEquals(List.of(Compressor.class, Maximum.class, Plug.class), this.ref.getList(pu.getEffects()));
        assertEquals(Set.of(Maximum.class), ref.getSet(pu.getEffectAtPosition(2).getConnectedInputs()));
        // adding at the last position using addEffectAtPosition
        pu.addEffectAtPosition(new Reverb(AudioContextManager.getAudioContext()), 3);
        assertEquals(List.of(Compressor.class, Maximum.class, Plug.class, Reverb.class),
                this.ref.getList(pu.getEffects()));
        assertEquals(Set.of(Plug.class), this.ref.getSet(pu.getEffectAtPosition(3).getConnectedInputs()));
    }

    @Test
    public void testOutOfBoundAddition() {
        // adding at an out-of-bound position
        pu.addEffectAtPosition(new Reverb(AudioContextManager.getAudioContext()), -1);
        pu.addEffectAtPosition(new Reverb(), 4);
        assertEquals(List.of(Compressor.class), this.ref.getList(pu.getEffects()));
    }
}
