package channel;

import daw.core.audioprocessing.*;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnitRemoveAndReplace {

    private TestReflection ref = new TestReflection();
    private ProcessingUnit pu = new BasicProcessingUnitBuilder()
            .reverb(1)
            .highPassFilter(1)
            .gate(2)
            .lowPassFilter(1)
            .build();

    @Test
    public void testCorrectRemoval() {
        this.pu.removeEffectAtPosition(2);
        assertEquals(List.of(DigitalReverb.class, HighPassFilter.class, LowPassFilter.class),
                this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(HighPassFilter.class), this.ref.getSet(this.pu.getEffectAtPosition(2).getConnectedInputs()));
        this.pu.removeEffectAtPosition(0);
        assertEquals(List.of(HighPassFilter.class, LowPassFilter.class), this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(), this.ref.getSet(this.pu.getEffectAtPosition(0).getConnectedInputs()));
    }

    @Test
    public void testWrongRemoval() {
        try {
            this.pu.removeEffectAtPosition(6);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            final var size = this.pu.getEffects().size();
            for (int i=0; i<size; i++) {
                this.pu.removeEffectAtPosition(0);
            }
            this.pu.removeEffectAtPosition(0);
            fail();
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testCorrectReplacement() {
        this.pu.replace(3, new DigitalReverb(1));
        assertEquals(List.of(DigitalReverb.class, HighPassFilter.class, Gate.class, DigitalReverb.class),
                this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(Gate.class), this.ref.getSet(this.pu.getEffectAtPosition(3).getConnectedInputs()));
        this.pu.replace(0, new Compression(1));
        assertEquals(List.of(Compression.class, HighPassFilter.class, Gate.class, DigitalReverb.class),
                this.ref.getList(this.pu.getEffects()));
        assertEquals(Set.of(), this.ref.getSet(this.pu.getEffectAtPosition(0).getConnectedInputs()));
    }

    @Test
    public void testWrongReplacement() {
        try {
            this.pu.replace(-1, new Compression(1));
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            this.pu.replace(5, new DigitalReverb(1));
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testSidechainingRemoval() {
        this.pu.removeSidechaining();
        assertFalse(this.pu.isSidechainingPresent());
        assertEquals(Set.of(), this.ref.getSet(this.pu.getEffectAtPosition(0).getConnectedInputs()));
        this.pu.addSidechaining(new SidechainingImpl(new SamplePlayer(1), 1));
        assertEquals(Set.of(), this.ref.getSet(this.pu.getEffectAtPosition(0).getConnectedInputs()));
    }

}
