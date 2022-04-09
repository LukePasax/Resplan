package daw;

import daw.core.channel.BasicProcessingUnit;
import daw.core.channel.ProcessingUnit;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class TestProcessingUnit {

    @Test
    public void testInitialization() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Panner(), new Compressor(),
                new CrossoverFilter(), new Reverb()));
        assertEquals(List.of(Panner.class, Compressor.class, CrossoverFilter.class, Reverb.class),
                this.reflection(pu.getEffects()));
        assertEquals(List.of(Compressor.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(2).getConnectedInputs())));
        try {
            new BasicProcessingUnit(List.of());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            new BasicProcessingUnit(null);
            fail();
        } catch(NullPointerException ignored) {
        }
    }

    @Test
    public void testAdding() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Compressor()));
        pu.addEffect(new Glide());
        assertEquals(List.of(Compressor.class, Glide.class), this.reflection(pu.getEffects()));
        pu.addEffectAtPosition(new Maximum(), 1);
        final var list = List.of(Compressor.class, Maximum.class, Glide.class);
        assertEquals(list, this.reflection(pu.getEffects()));
        pu.addEffectAtPosition(new Plug(new AudioContext()), -1);
        pu.addEffectAtPosition(new Reverb(), 4);
        assertEquals(list, this.reflection(pu.getEffects()));
        pu.addEffectAtPosition(new Reverb(), 3);
        assertEquals(List.of(Compressor.class, Maximum.class, Glide.class, Reverb.class),
                this.reflection(pu.getEffects()));
    }

    private List<Class> reflection(List<UGen> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toList());
    }
}
