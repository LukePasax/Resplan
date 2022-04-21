package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BasicProcessingUnit implements ProcessingUnit {

    private final LinkedList<RPEffect> effects = new LinkedList<>();

    public BasicProcessingUnit(final List<RPEffect> effects) {
        if (effects != null && !effects.isEmpty()) {
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
        this.effects.peekLast().connectTo(u);
    }

    @Override
    public List<RPEffect> getEffects() {
        return Collections.unmodifiableList(this.effects);
    }

    @Override
    public UGen getEffectAtPosition(int index) {
        return this.effects.get(index);
    }

    @Override
    public void addEffect(RPEffect u) {
        this.addEffectAtPosition(u, this.effects.size());
    }

    @Override
    public void addEffectAtPosition(RPEffect u, int index) {
        if (this.checkInsAndOuts(u, index)) {
            if (index >= 0 && index <= this.effects.size()) {
                this.effects.add(index, u);
                if (index > 0) {
                    this.connectEffects(this.effects.get(index-1), u);
                }
                if (index < this.effects.size() - 1) {
                    this.connectEffects(u, this.effects.get(index+1));
                }
            }
        } else {
            throw new IllegalArgumentException("UGen " + u.getClass() +
                    " has either 0 inputs or 0 outputs, therefore it cannot be added to the sequence");
        }
    }

    private boolean checkInsAndOuts(RPEffect u, int index) {
        return u.getOuts() != 0 && u.getIns() != 0;
    }

    private void connectEffects(RPEffect from, RPEffect to) {
        to.clearInputConnections();
        to.addInput(from);
    }

    @Override
    public void removeEffectAtPosition(int index) {
        if (this.effects.size()>1) {
            if (index != this.effects.size()-1) {
                this.effects.get(index + 1).clearInputConnections();
                if (index != 0) {
                    this.effects.get(index+1).addInput(this.effects.get(index-1));
                }
            }
            this.effects.remove(index);
        } else {
            throw new IllegalStateException("Cannot perform this operation when there is only one effect stored");
        }
    }

    @Override
    public void moveEffect(int currentIndex, int newIndex) {
        if (currentIndex >= 0 && newIndex >= 0 && currentIndex <= this.effects.size()-1 &&
                newIndex <= this.effects.size()-1) {
            final var temp = this.effects.get(currentIndex);
            this.effects.remove(currentIndex);
            this.addEffectAtPosition(temp, newIndex);
        } else {
            throw new IllegalArgumentException("The given indexes are not legal");
        }
    }

    @Override
    public void swapEffects(int firstIndex, int secondIndex) {
        if (firstIndex >= 0 && secondIndex >= 0 && firstIndex <= this.effects.size()-1 &&
                secondIndex <= this.effects.size()-1) {
            this.moveEffect(firstIndex, secondIndex);
            this.moveEffect(secondIndex - 1, firstIndex);
        } else {
            throw new IllegalArgumentException("The given indexes are not legal");
        }
    }

    @Override
    public void replace(int index, RPEffect u) {
        try {
            this.removeEffectAtPosition(index);
            this.addEffectAtPosition(u, index);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException("The given index is not legal");
        }
    }

}
