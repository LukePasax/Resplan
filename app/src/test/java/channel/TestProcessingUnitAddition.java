package channel;

import daw.utilities.AudioContextManager;
import daw.core.audioprocessing.*;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitAddition {

    private final TestUtility testUtility = new TestUtility();
    private final ProcessingUnit pu = new BasicProcessingUnitBuilder().gate(1).compressor(1).build();

    @Test
    public void testCorrectAddition() {
        // adding at the last position using addEffect
        this.pu.addEffect(new DigitalReverb(1));
        assertEquals(List.of(Gate.class, Compression.class, DigitalReverb.class), this.testUtility.getList(this.pu.getEffects()));
        // adding at a given position
        this.pu.addEffectAtPosition(new DigitalReverb(1), 1);
        assertEquals(List.of(Gate.class, DigitalReverb.class, Compression.class, DigitalReverb.class),
                this.testUtility.getList(this.pu.getEffects()));
        assertTrue(this.testUtility.effectsAreConnected(this.pu.getEffectAtPosition(1), this.pu.getEffectAtPosition(2)));
        // adding at the last position using addEffectAtPosition
        this.pu.addEffectAtPosition(new HighPassFilter(1), 4);
        assertEquals(List.of(Gate.class, DigitalReverb.class, Compression.class, DigitalReverb.class, HighPassFilter.class),
                this.testUtility.getList(this.pu.getEffects()));
        assertTrue(this.testUtility.effectsAreConnected(this.pu.getEffectAtPosition(3), this.pu.getEffectAtPosition(4)));
    }

    @Test
    public void testOutOfBoundAddition() {
        // adding at an out-of-bound position
        try {
            this.pu.addEffectAtPosition(new DigitalReverb(1), -1);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            this.pu.addEffectAtPosition(new DigitalReverb(2), 4);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        assertEquals(this.testUtility.getList(this.pu.getEffects()), this.testUtility.getList(this.pu.getEffects()));
    }

    @Test
    public void testSidechainAddition() {
        this.pu.addSidechaining(new SidechainingImpl(new SamplePlayer(AudioContextManager.getAudioContext(), 2),2));
        assertTrue(this.pu.isSidechainingPresent());
        assertEquals(List.of(SidechainingImpl.class, Gate.class, Compression.class), this.testUtility.getList(this.pu.getEffects()));
        assertTrue(this.testUtility.effectsAreConnected(this.pu.getEffectAtPosition(0), this.pu.getEffectAtPosition(1)));
    }

}
