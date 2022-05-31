package channel;

import daw.utilities.AudioContextManager;
import daw.core.audioprocessing.*;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitAddition {

    private final TestReflection ref = new TestReflection();
    private final ProcessingUnit pu = new BasicProcessingUnitBuilder().gate(1).compressor(1).build();

    @Test
    public void testCorrectAddition() {
        // adding at the last position using addEffect
        this.pu.addEffect(new DigitalReverb(1));
        assertEquals(List.of(Gate.class, Compression.class, DigitalReverb.class), this.ref.getList(this.pu.getEffects()));
        // adding at a given position
        this.pu.addEffectAtPosition(new DigitalReverb(1), 1);
        assertEquals(List.of(Gate.class, DigitalReverb.class, Compression.class, DigitalReverb.class),
                this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(DigitalReverb.class), this.ref.getSet(this.pu.getEffectAtPosition(2).getConnectedInputs()));
        // adding at the last position using addEffectAtPosition
        this.pu.addEffectAtPosition(new HighPassFilter(1, 100.0f), 4);
        assertEquals(List.of(Gate.class, DigitalReverb.class, Compression.class, DigitalReverb.class, HighPassFilter.class),
                this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(DigitalReverb.class), this.ref.getSet(this.pu.getEffectAtPosition(4).getConnectedInputs()));
    }

    @Test
    public void testOutOfBoundAddition() {
        // adding at an out-of-bound position
        final var currPu = this.pu;
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
        assertEquals(this.ref.getList(currPu.getEffects()), this.ref.getList(this.pu.getEffects()));
    }

    @Test
    public void testSidechainAddition() {
        this.pu.addSidechaining(new BasicSidechaining(new SamplePlayer(AudioContextManager.getAudioContext(), 2),2));
        assertTrue(this.pu.isSidechainingPresent());
        assertEquals(List.of(BasicSidechaining.class, Gate.class, Compression.class), this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(BasicSidechaining.class), this.ref.getSet(this.pu.getEffectAtPosition(1).getConnectedInputs()));
    }

}
