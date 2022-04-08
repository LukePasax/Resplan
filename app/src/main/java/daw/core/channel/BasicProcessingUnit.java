package daw.core.channel;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BasicProcessingUnit implements ProcessingUnit {

    private final List<UGen> effects;

    public BasicProcessingUnit(final List<UGen> effects) {
        /*
         * TO DO: constructor should call the addEffect method for every element in the given list,
         * so as to connect the inputs and outputs of the UGens.
         */
        this.effects = new LinkedList<>(effects);
    }

    @Override
    public List<UGen> getEffects() {
        return Collections.unmodifiableList(this.effects);
    }

    @Override
    public UGen getEffectAtPosition(int index) {
        return null;
    }

    @Override
    public void addEffect(UGen u) {

    }

    @Override
    public void addEffectAtPosition(UGen u, int index) {

    }

    @Override
    public void moveEffect(int currentIndex, int newIndex) {

    }

    @Override
    public void swapEffects(int firstIndex, int secondIndex) {

    }

    @Override
    public void replace(int index, UGen u) {

    }

    @Override
    public void addInput(Gain g) {

    }

    @Override
    public Optional<Gain> getOutput() {
        return Optional.empty();
    }

}
