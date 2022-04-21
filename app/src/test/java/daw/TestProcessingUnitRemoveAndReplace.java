package daw;

import daw.core.audioprocessing.BasicProcessingUnit;
import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.ugens.Minimum;
import net.beadsproject.beads.ugens.MonoPlug;
import net.beadsproject.beads.ugens.Phasor;
import net.beadsproject.beads.ugens.Reverb;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProcessingUnitRemoveAndReplace {

    private TestReflection ref = new TestReflection();

    @Test
    public void testRemovingAndReplacing() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Reverb(), new Phasor(), new Minimum()));
        // replacing at an out-of-bound position
        try {
            pu.replace(3, new MonoPlug());
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        // removing at an out-of-bound position
        try {
            pu.removeEffectAtPosition(3);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        // replacing
        pu.replace(1, new MonoPlug());
        assertEquals(List.of(Reverb.class, MonoPlug.class, Minimum.class), this.ref.getList(pu.getEffects()));
        assertEquals(List.of(Reverb.class), this.ref.getList(new ArrayList<>(
                pu.getEffectAtPosition(1).getConnectedInputs())));
        assertEquals(List.of(MonoPlug.class), this.ref.getList(new ArrayList<>(
                pu.getEffectAtPosition(2).getConnectedInputs())));
        // removing
        pu.removeEffectAtPosition(1);
        assertEquals(List.of(Reverb.class, Minimum.class), this.ref.getList(pu.getEffects()));
        assertEquals(List.of(Reverb.class), this.ref.getList(new ArrayList<>(
                pu.getEffectAtPosition(1).getConnectedInputs())));
        pu.removeEffectAtPosition(0);
        assertEquals(List.of(Minimum.class), this.ref.getList(pu.getEffects()));
        assertEquals(List.of(), this.ref.getList(new ArrayList<>(
                pu.getEffectAtPosition(0).getConnectedInputs())));
        // removing when there is only one effect
        try {
            pu.removeEffectAtPosition(0);
        } catch (IllegalStateException ex) {
            assertEquals(List.of(Minimum.class), this.ref.getList(pu.getEffects()));
        }
    }
}
