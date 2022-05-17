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
    private Optional<BasicSidechaining> sidechain;

    /**
     * Constructs a {@link ProcessingUnit} that is not sidechained.
     * @param effects the effects to be added to the sequence. Order of the list in input is preserved.
     * @throws IllegalArgumentException if the list is null or contains no effects.
     */
    protected BasicProcessingUnit(final List<RPEffect> effects) {
        this.sidechain = Optional.empty();
        if (effects != null && !effects.isEmpty()) {
            for (final var elem : effects) {
                this.addEffect(elem);
            }
        } else {
            throw new IllegalArgumentException("Cannot instantiate with an empty sequence.");
        }
    }

    /**
     * {@inheritDoc}
     * @param g the {@link Gain} that represents the audio source.
     */
    @Override
    public void addInput(Gain g) {
        if (this.isSidechainingPresent()) {
            this.sidechain.get().addInput(g);
        } else {
            this.getEffectAtPosition(0).addInput(g);
        }
    }

    /**
     * {@inheritDoc}
     * @param u the {@link UGen} the UGen to which the output must be connected to.
     */
    @Override
    public void connect(UGen u) {
        this.getEffectAtPosition(this.effects.size()-1).connectTo(u);
    }

    /**
     * {@inheritDoc}
     * @param s the compressor that performs the sidechaining.
     */
    @Override
    public void addSidechaining(BasicSidechaining s) {
        if (!this.isSidechainingPresent()) {
            this.sidechain = Optional.of(s);
            this.getEffectAtPosition(0).addInput(this.sidechain.get());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSidechaining() {
        if (this.isSidechainingPresent()) {
            this.getEffectAtPosition(0).removeAllConnections(this.sidechain.get());
        }
        this.sidechain = Optional.empty();
    }

    /**
     * {@inheritDoc}
     * @return true if the {@link ProcessingUnit} is sidechained.
     */
    @Override
    public boolean isSidechainingPresent() {
        return this.sidechain.isPresent();
    }

    /**
     * {@inheritDoc}
     * @return the current {@link List} of effects.
     */
    @Override
    public List<RPEffect> getEffects() {
        return Collections.unmodifiableList(this.effects);
    }

    /**
     * {@inheritDoc}
     * @param index a position in the sequence.
     * @return the {@link RPEffect} that is placed at the given position.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    @Override
    public RPEffect getEffectAtPosition(int index) {
        try {
            return this.effects.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * @param u the {@link RPEffect} that represents the effect to be added.
     */
    @Override
    public void addEffect(RPEffect u) {
        this.addEffectAtPosition(u, this.effects.size());
    }

    /**
     * {@inheritDoc}
     * @param u the {@link RPEffect} that represents the effect to be added.
     * @param index a position in the sequence.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    @Override
    public void addEffectAtPosition(RPEffect u, int index) {
        if (index >= 0 && index <= this.effects.size()) {
            this.effects.add(index, u);
            if (index > 0) {
                this.connectEffects(this.effects.get(index-1), u);
            } else if (this.isSidechainingPresent()) {
                this.getEffectAtPosition(0).addInput(this.sidechain.get());
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

    /**
     * {@inheritDoc}
     * @param index a position in the sequence.
     * @throws IllegalStateException if there is only one effect stored when this method is called.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    @Override
    public void removeEffectAtPosition(int index) {
        if (index >= 0 && index <= this.effects.size()-1) {
            if (this.effects.size() > 1) {
                if (index != this.effects.size() - 1) {
                    this.getEffectAtPosition(index + 1).removeAllConnections(this.getEffectAtPosition(index));
                    if (index != 0) {
                        this.getEffectAtPosition(index + 1).addInput(this.getEffectAtPosition(index - 1));
                    } else if (this.isSidechainingPresent()) {
                        this.getEffectAtPosition(index + 1).addInput(this.sidechain.get());
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

    /**
     * {@inheritDoc}
     * @param currentIndex the position of the effect to be moved.
     * @param newIndex the position to which the effect must be moved.
     * @throws IllegalArgumentException if one or both of the given positions are out of bounds.
     */
    @Override
    public void moveEffect(int currentIndex, int newIndex) {
        if (currentIndex >= 0 && newIndex >= 0 && currentIndex <= this.effects.size()-1 &&
                newIndex <= this.effects.size()-1) {
            final var temp = this.getEffectAtPosition(currentIndex);
            this.removeEffectAtPosition(currentIndex);
            this.addEffectAtPosition(temp, newIndex);
        } else {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * @param index1 the position of an effect that must be swapped.
     * @param index2 the position of the other effect that must be swapped.
     * @throws IllegalArgumentException if one or both of the given positions are out of bounds.
     */
    @Override
    public void swapEffects(int index1, int index2) {
        if (index1 >= 0 && index2 >= 0 && index1 <= this.effects.size()-1 &&
                index2 <= this.effects.size()-1) {
            this.moveEffect(index1, index2);
            this.moveEffect(index2 - 1, index1);
        } else {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * @param index the index of the effect that must be replaced.
     * @param u the effect that serves as a replacement.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
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
