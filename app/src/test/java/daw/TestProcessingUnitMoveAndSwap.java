package daw;

import daw.core.channel.BasicProcessingUnit;
import daw.core.channel.ProcessingUnit;
import net.beadsproject.beads.ugens.Minimum;
import net.beadsproject.beads.ugens.Phasor;
import net.beadsproject.beads.ugens.Reverb;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProcessingUnitMoveAndSwap {

    private TestReflection ref = new TestReflection();

    @Test
    public void testMovingAndSwapping() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Reverb(), new Phasor(), new Minimum()));
        // swapping two effects
        pu.swapEffects(0,2);
        assertEquals(List.of(Minimum.class, Phasor.class, Reverb.class), this.ref.getList(pu.getEffects()));
        // swapping with an out-of-bound index
        try {
            pu.swapEffects(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(Minimum.class, Phasor.class, Reverb.class), this.ref.getList(pu.getEffects()));
        }
        // moving an effect
        pu.moveEffect(0,2);
        assertEquals(List.of(Phasor.class, Reverb.class, Minimum.class), this.ref.getList(pu.getEffects()));
        // moving to an out-of-bound index
        try {
            pu.moveEffect(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(Phasor.class, Reverb.class, Minimum.class), this.ref.getList(pu.getEffects()));
        }
    }
}
