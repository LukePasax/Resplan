package daw.core.audioprocessing;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import java.util.*;

/**
 * This class represents a basic implementation of {@link ProcessingUnit}.
 * This particular implementation instantiates a {@link ProcessingUnit} so that it is not sidechained.
 * The only way to get an object of this class is to use the {@link ProcessingUnitBuilder}.
 */
public class BasicProcessingUnit implements ProcessingUnit {

    private static final String ILLEGAL_INDEX_ERROR = "The given index(es) is (are) not legal.";

    private final LinkedList<RPEffect> effects = new LinkedList<>();
    private Optional<Sidechaining> sidechain;

    protected BasicProcessingUnit(final List<RPEffect> effects) {
        if (effects != null && !effects.isEmpty()) {
            for (final var elem : effects) {
                this.addEffect(elem);
            }
        } else {
            throw new IllegalArgumentException("Cannot instantiate with an empty sequence.");
        }
        this.sidechain = Optional.empty();
    }

    @Override
    public void addInput(Gain g) {
        if (this.isSidechainingPresent()) {
            this.sidechain.get().addInput(g);
        } else {
            this.getEffectAtPosition(0).addInput(g);
        }
    }

    @Override
    public void connect(UGen u) {
        this.getEffectAtPosition(this.effects.size()-1).connectTo(u);
    }

    @Override
    public void addSidechaining(Sidechaining s) {
        if (!this.isSidechainingPresent()) {
            this.sidechain = Optional.of(s);
            this.getEffectAtPosition(0).addInput(this.sidechain.get());
        }
    }

    @Override
    public void removeSidechaining() {
        this.sidechain = Optional.empty();
    }

    @Override
    public boolean isSidechainingPresent() {
        return this.sidechain.isPresent();
    }

    @Override
    public List<RPEffect> getEffects() {
        return Collections.unmodifiableList(this.effects);
    }

    @Override
    public RPEffect getEffectAtPosition(int index) {
        try {
            return this.effects.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    @Override
    public void addEffect(RPEffect u) {
        this.addEffectAtPosition(u, this.effects.size());
    }

    @Override
    public void addEffectAtPosition(RPEffect u, int index) {
        if (index >= 0 && index <= this.effects.size()) {
            this.effects.add(index, u);
            if (index > 0) {
                this.connectEffects(this.effects.get(index-1), u);
            }
            if (index < this.effects.size() - 1) {
                this.connectEffects(u, this.effects.get(index+1));
            }
        } else {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    private void connectEffects(RPEffect from, RPEffect to) {
        to.clearInputConnections();
        to.addInput(from);
    }

    @Override
    public void removeEffectAtPosition(int index) {
        if (index >= 0 && index <= this.effects.size()-1) {
            if (this.effects.size() > 1) {
                if (index != this.effects.size() - 1) {
                    this.effects.get(index + 1).clearInputConnections();
                    if (index != 0) {
                        this.effects.get(index + 1).addInput(this.effects.get(index - 1));
                    }
                }
                this.effects.remove(index);
            } else {
                throw new IllegalStateException("Cannot perform this operation when there is only one effect stored.");
            }
        } else {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
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
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    @Override
    public void swapEffects(int firstIndex, int secondIndex) {
        if (firstIndex >= 0 && secondIndex >= 0 && firstIndex <= this.effects.size()-1 &&
                secondIndex <= this.effects.size()-1) {
            this.moveEffect(firstIndex, secondIndex);
            this.moveEffect(secondIndex - 1, firstIndex);
        } else {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    @Override
    public void replace(int index, RPEffect u) {
        try {
            this.removeEffectAtPosition(index);
            this.addEffectAtPosition(u, index);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

}
