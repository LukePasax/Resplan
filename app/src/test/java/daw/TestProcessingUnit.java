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
        // initializing with multiple UGens
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Panner(), new Compressor(),
                new CrossoverFilter(), new Reverb()));
        assertEquals(List.of(Panner.class, Compressor.class, CrossoverFilter.class, Reverb.class),
                this.reflection(pu.getEffects()));
        /*
        assertEquals(List.of(Compressor.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(2).getConnectedInputs())));
         */
        // initializing with no UGens
        try {
            new BasicProcessingUnit(List.of());
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        // initializing with no List
        try {
            new BasicProcessingUnit(null);
            fail();
        } catch(IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testAdding() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Compressor()));
        // adding at the last position using addEffect
        pu.addEffect(new Glide());
        assertEquals(List.of(Compressor.class, Glide.class), this.reflection(pu.getEffects()));
        /*
        assertEquals(List.of(Compressor.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(1).getConnectedInputs())));
         */
        // adding at a given position
        pu.addEffectAtPosition(new Maximum(), 1);
        final var list = List.of(Compressor.class, Maximum.class, Glide.class);
        assertEquals(list, this.reflection(pu.getEffects()));
        System.out.println(pu.getEffects());
        /*
        assertEquals(List.of(Maximum.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(2).getConnectedInputs())));
         */
        // adding at an out-of-bound position
        pu.addEffectAtPosition(new Plug(new AudioContext()), -1);
        pu.addEffectAtPosition(new Reverb(), 4);
        assertEquals(list, this.reflection(pu.getEffects()));
        // adding at the last position using addEffectAtPosition
        pu.addEffectAtPosition(new Reverb(), 3);
        assertEquals(List.of(Compressor.class, Maximum.class, Glide.class, Reverb.class),
                this.reflection(pu.getEffects()));
    }

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
        assertEquals(List.of(Reverb.class, MonoPlug.class, Minimum.class), this.reflection(pu.getEffects()));
        /*
        assertEquals(List.of(Reverb.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(1).getConnectedInputs())));
        assertEquals(List.of(MonoPlug.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(2).getConnectedInputs())));
         */
        // removing
        pu.removeEffectAtPosition(1);
        assertEquals(List.of(Reverb.class, Minimum.class), this.reflection(pu.getEffects()));
        /*
        assertEquals(List.of(Reverb.class), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(1).getConnectedInputs())));
         */
        pu.removeEffectAtPosition(0);
        assertEquals(List.of(Minimum.class), this.reflection(pu.getEffects()));
        /*
        assertEquals(List.of(), this.reflection(new ArrayList<>(
                pu.getEffectAtPosition(0).getConnectedInputs())));
         */
        // removing when there is only one effect
        try {
            pu.removeEffectAtPosition(0);
        } catch (IllegalStateException ex) {
            assertEquals(List.of(Minimum.class), this.reflection(pu.getEffects()));
        }
    }

    @Test
    public void testMovingAndSwapping() {
        final ProcessingUnit pu = new BasicProcessingUnit(List.of(new Reverb(), new Phasor(), new Minimum()));
        // swapping two effects
        pu.swapEffects(0,2);
        assertEquals(List.of(Minimum.class, Phasor.class, Reverb.class), this.reflection(pu.getEffects()));
        // swapping with an out-of-bound index
        try {
            pu.swapEffects(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(Minimum.class, Phasor.class, Reverb.class), this.reflection(pu.getEffects()));
        }
        // moving an effect
        pu.moveEffect(0,2);
        assertEquals(List.of(Phasor.class, Reverb.class, Minimum.class), this.reflection(pu.getEffects()));
        // moving to an out-of-bound index
        try {
            pu.moveEffect(0, 3);
        } catch (IllegalArgumentException ex) {
            assertEquals(List.of(Phasor.class, Reverb.class, Minimum.class), this.reflection(pu.getEffects()));
        }
    }

    private List<Class> reflection(List<UGen> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toList());
    }
}
