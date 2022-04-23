package channel;

import Resplan.AudioContextManager;
import daw.core.audioprocessing.BasicProcessingUnit;
import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProcessingUnitMoveAndSwap {

    private TestReflection ref = new TestReflection();
    private ProcessingUnit pu = new BasicProcessingUnit(List.of(
            new Reverb(AudioContextManager.getAudioContext()),
            new OnePoleFilter(AudioContextManager.getAudioContext(), 1000.0f),
            new Minimum(AudioContextManager.getAudioContext())));

    @Test
    public void testCorrectSwapping() {
        // swapping two effects
        pu.swapEffects(0,2);
        assertEquals(List.of(Minimum.class, OnePoleFilter.class, Reverb.class), this.ref.getList(pu.getEffects()));
    }

    @Test
    public void testCorrectMoving() {
        // moving an effect
        pu.moveEffect(0,2);
        assertEquals(List.of(OnePoleFilter.class, Minimum.class, Reverb.class), this.ref.getList(pu.getEffects()));
    }

    @Test
    public void testIncorrectSwapping() {
        // swapping with an out-of-bound index
        try {
            pu.swapEffects(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(Reverb.class, OnePoleFilter.class, Minimum.class), this.ref.getList(pu.getEffects()));
        }
    }

    @Test
    public void testIncorrectMoving() {
        // moving to an out-of-bound index
        try {
            pu.moveEffect(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(Reverb.class, OnePoleFilter.class, Minimum.class), this.ref.getList(pu.getEffects()));
        }
    }
}
