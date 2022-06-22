package channel;

import daw.core.audioprocessing.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestProcessingUnitMoveAndSwap {

    private final TestUtility ref = new TestUtility();
    private final ProcessingUnit pu = new BasicProcessingUnitBuilder()
            .reverb(2)
            .lowPassFilter(2)
            .highPassFilter(2)
            .build();

    @Test
    public void testCorrectSwapping() {
        // swapping two effects
        pu.swapEffects(0,2);
        assertEquals(List.of(HighPassFilter.class, LowPassFilter.class, DigitalReverb.class),
                this.ref.getList(pu.getEffects()));
        assertTrue(this.ref.effectsAreConnected(this.pu.getEffectAtPosition(0), this.pu.getEffectAtPosition(1)));
        assertTrue(this.ref.effectsAreConnected(this.pu.getEffectAtPosition(1), this.pu.getEffectAtPosition(2)));
    }

    @Test
    public void testCorrectMoving() {
        // moving an effect
        pu.moveEffect(0,2);
        assertEquals(List.of(LowPassFilter.class, HighPassFilter.class, DigitalReverb.class),
                this.ref.getList(pu.getEffects()));
        assertTrue(this.ref.effectsAreConnected(this.pu.getEffectAtPosition(0), this.pu.getEffectAtPosition(1)));
        assertTrue(this.ref.effectsAreConnected(this.pu.getEffectAtPosition(1), this.pu.getEffectAtPosition(2)));
    }

    @Test
    public void testIncorrectSwapping() {
        // swapping with an out-of-bound index
        try {
            pu.swapEffects(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(DigitalReverb.class, LowPassFilter.class, HighPassFilter.class),
                    this.ref.getList(pu.getEffects()));
        }
    }

    @Test
    public void testIncorrectMoving() {
        // moving to an out-of-bound index
        try {
            pu.moveEffect(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(DigitalReverb.class, LowPassFilter.class, HighPassFilter.class),
                    this.ref.getList(pu.getEffects()));
        }
    }
}
