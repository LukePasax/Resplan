package daw.core.channel;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BasicProcessingUnit implements ProcessingUnit {

    private final LinkedList<UGen> effects = new LinkedList<>();

    public BasicProcessingUnit(final List<UGen> effects) {
        if (!effects.isEmpty()) {
            for (final var elem : effects) {
                this.addEffect(elem);
            }
        } else {
            throw new IllegalArgumentException("Cannot instantiate with an empty sequence");
        }
    }

    @Override
    public void addInput(Gain g) {
        this.effects.peek().addInput(g);
    }

    @Override
    public void connect(UGen u) {
        if (this.effects.size()>0) {
            this.effects.peekLast().connectTo(u);
        }
    }

    @Override
    public List<UGen> getEffects() {
        return Collections.unmodifiableList(this.effects);
    }

    @Override
    public UGen getEffectAtPosition(int index) {
        return this.effects.get(index);
    }

    @Override
    public void addEffect(UGen u) {
        this.addEffectAtPosition(u, this.effects.size());
    }

    @Override
    public void addEffectAtPosition(UGen u, int index) {
        if (index >= 0 && index <= this.effects.size()) {
            this.effects.add(index, u);
            if (index > 0) {
                u.addInput(this.effects.get(index-1));
            }
            if (index < this.effects.size()-1) {
                this.effects.get(index+1).addInput(u);
            }
        }
    }

    @Override
    public void removeEffectAtPosition(int index) {
        if (this.effects.size()>1) {
            this.effects.remove(index);
        }
    }

    @Override
    public void moveEffect(int currentIndex, int newIndex) {
        final var temp = this.effects.get(currentIndex);
        this.effects.remove(currentIndex);
        this.addEffectAtPosition(temp, newIndex);
    }

    @Override
    public void swapEffects(int firstIndex, int secondIndex) {
        this.moveEffect(firstIndex, secondIndex);
        this.moveEffect(secondIndex+1, firstIndex);
    }

    @Override
    public void replace(int index, UGen u) {
        this.effects.remove(index);
        this.effects.add(index, u);
    }

}
