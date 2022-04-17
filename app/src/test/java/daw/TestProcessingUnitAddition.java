package daw;

import Resplan.AudioContextManager;
import daw.core.channel.BasicProcessingUnit;
import daw.core.channel.ProcessingUnit;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProcessingUnitAddition {

    private TestReflection ref = new TestReflection();

    @Test
    public void testAdding() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Compressor()));
        // adding an illegal UGen
        try {
            pu.addEffect(new Glide(AudioContextManager.getAudioContext()));
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        // adding at the last position using addEffect
        pu.addEffect(new Plug(AudioContextManager.getAudioContext()));
        assertEquals(List.of(Compressor.class, Plug.class), this.ref.getList(pu.getEffects()));
        // adding at a given position
        pu.addEffectAtPosition(new Maximum(), 1);
        final var list = List.of(Compressor.class, Maximum.class, Plug.class);
        assertEquals(list, this.ref.getList(pu.getEffects()));
        assertEquals(Set.of(Maximum.class, Compressor.class), pu.getEffectAtPosition(2)
                .getConnectedInputs().stream().map(Object::getClass).collect(Collectors.toSet()));
        // adding at an out-of-bound position
        pu.addEffectAtPosition(new Reverb(AudioContextManager.getAudioContext()), -1);
        pu.addEffectAtPosition(new Reverb(), 4);
        assertEquals(list, this.ref.getList(pu.getEffects()));
        // adding at the last position using addEffectAtPosition
        pu.addEffectAtPosition(new Reverb(), 3);
        assertEquals(List.of(Compressor.class, Maximum.class, Plug.class, Reverb.class),
                this.ref.getList(pu.getEffects()));
    }
}
