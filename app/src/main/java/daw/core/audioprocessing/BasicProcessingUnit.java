package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import java.util.*;

/**
 * This class represents a basic implementation of {@link ProcessingUnit}.
 * This particular implementation instantiates a {@link ProcessingUnit} so that it is not sidechained.
 * The only way to get an object of this class is to use the {@link ProcessingUnitBuilder}.
 */
public class BasicProcessingUnit implements ProcessingUnit {

    private static final String ILLEGAL_INDEX_ERROR = "The given index is not legal.";

    private final LinkedList<RPEffect> effects = new LinkedList<>();

    /**
     * Constructs a {@link ProcessingUnit} that is sidechained.
     * @param effects the effects to be added to the sequence. Order of the list in input is preserved.
     */
    @JsonCreator
    protected BasicProcessingUnit(@JsonProperty("effects") final List<RPEffect> effects) {
        for (final var effect: effects) {
            if (effect instanceof BasicSidechaining) {
                this.addSidechaining((BasicSidechaining) effect);
            } else {
                this.addEffect(effect);
            }
        }
    }

    /**
     * {@inheritDoc}
     * @param g the {@link Gain} that represents the audio source.
     */
    @Override
    public void addInput(Gain g) {
        this.getEffectAtPosition(0).addInput(g);
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
            this.addEffectAtPosition(s,0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSidechaining() {
        if (this.isSidechainingPresent()) {
            this.removeEffectAtPosition(0);
        }
    }

    /**
     * {@inheritDoc}
     * @return true if the {@link ProcessingUnit} is sidechained.
     */
    @Override
    public boolean isSidechainingPresent() {
        if (!this.effects.isEmpty()) {
            return this.effects.get(0) instanceof BasicSidechaining;
        }
        return false;
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
     * @throws IllegalArgumentException if the given position is out of bounds or if the given effect
     * is an instantiation of {@link Sidechaining}.
     */
    @Override
    public void addEffect(RPEffect u) {
        this.addEffectAtPosition(u, this.effects.size());
    }

    /**
     * {@inheritDoc}
     * @param u the {@link RPEffect} that represents the effect to be added.
     * @param index a position in the sequence.
     * @throws IllegalArgumentException if the given position is out of bounds or if the given effect
     * is an instantiation of {@link Sidechaining}.
     */
    @Override
    public void addEffectAtPosition(RPEffect u, int index) {
        if (u instanceof BasicSidechaining && index != 0) {
            throw new IllegalArgumentException("To add a sidechained compressor use addSidechaining");
        }
        if (index >= 0 && index <= this.effects.size()) {
            if (index == 0 && this.isSidechainingPresent()) {
                throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR +
                        "Position 0 is occupied by the sidechaining compressor.");
            } else {
                this.effects.add(index, u);
                if (index != 0) {
                    this.connectEffects(this.effects.get(index - 1), u);
                }
            }
            if (index < this.effects.size()-1) {
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
     * @throws IllegalStateException if there is only one {@link AudioElement} stored when this method is called.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    @Override
    public void removeEffectAtPosition(int index) {
        if (index >= 0 && index <= this.effects.size()-1) {
            if (this.effects.size() > 1) {
                if (index != this.effects.size()-1) {
                    this.getEffectAtPosition(index + 1).removeAllConnections(this.getEffectAtPosition(index));
                    if (index != 0) {
                        this.getEffectAtPosition(index + 1).addInput(this.getEffectAtPosition(index - 1));
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
        if (currentIndex >= 0 &&
                newIndex >= 0 &&
                currentIndex <= this.effects.size()-1 &&
                newIndex <= this.effects.size()-1 &&
                !(this.getEffectAtPosition(currentIndex) instanceof BasicSidechaining) &&
                !(this.getEffectAtPosition(newIndex) instanceof BasicSidechaining)) {
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
        if (index1 >= 0 &&
                index2 >= 0 &&
                index1 <= this.effects.size()-1 &&
                index2 <= this.effects.size()-1 &&
                !(this.getEffectAtPosition(index1) instanceof BasicSidechaining) &&
                !(this.getEffectAtPosition(index2) instanceof BasicSidechaining)) {
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
