package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

/**
 * This class represents a basic implementation of {@link ProcessingUnit}.
 * This particular implementation instantiates a {@link ProcessingUnit} so that it is not sidechained.
 * The only way to get an object of this class is to use the {@link ProcessingUnitBuilder}.
 */
public final class BasicProcessingUnit implements ProcessingUnit {

    private static final String ILLEGAL_INDEX_ERROR = "The given index is not legal.";
    private static final int INS = 2;

    private final LinkedList<RPEffect> effects = new LinkedList<>();
    private final Gain gainIn;
    private final Gain gainOut;

    /**
     * Constructs a {@link ProcessingUnit} that is sidechained.
     * @param effects the effects to be added to the sequence. Order of the list in input is preserved.
     */
    @JsonCreator
    public BasicProcessingUnit(@JsonProperty("effects") final List<RPEffect> effects) {
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), INS, 1.0f);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), INS, 1.0f);
        for (final var effect: effects) {
            if (effect instanceof Sidechaining) {
                this.addSidechaining((SidechainingImpl) effect);
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
    public void addInput(final Gain g) {
        this.gainIn.addInput(g);
    }

    /**
     * {@inheritDoc}
     * @param u the {@link UGen} the UGen to which the output must be connected to.
     */
    @Override
    public void connect(final UGen u) {
        u.addInput(this.gainOut);
    }

    /**
     * {@inheritDoc}
     * @param s the compressor that performs the sidechaining.
     */
    @Override
    public void addSidechaining(final SidechainingImpl s) {
        if (!this.isSidechainingPresent()) {
            this.effects.add(0, s);
            s.getGainIn().addInput(this.gainIn);
            if (this.size() > 1) {
                this.connectEffects(s, this.getEffectAtPosition(1));
            } else {
                this.gainOut.clearInputConnections();
                this.gainOut.addInput(s.getGainOut());
            }
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
        // This check is useful to initialize the unit. After that it becomes useless.
        if (!this.effects.isEmpty()) {
            return this.effects.get(0) instanceof Sidechaining;
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
    public RPEffect getEffectAtPosition(final int index) {
        try {
            return this.effects.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int size() {
        return this.effects.size();
    }

    /**
     * {@inheritDoc}
     * @param u the {@link RPEffect} that represents the effect to be added.
     * @throws IllegalArgumentException if the given position is out of bounds or if the given effect
     * is an instantiation of {@link Sidechaining}.
     */
    @Override
    public void addEffect(final RPEffect u) {
        this.addEffectAtPosition(u, this.size());
    }

    /**
     * {@inheritDoc}
     * @param u the {@link RPEffect} that represents the effect to be added.
     * @param index a position in the sequence.
     * @throws IllegalArgumentException if the given position is out of bounds or if the given effect
     * is an instantiation of {@link Sidechaining}.
     */
    @Override
    public void addEffectAtPosition(final RPEffect u, final int index) {
        if (u instanceof Sidechaining) {
            throw new IllegalArgumentException("To add a sidechained compressor use addSidechaining.");
        }
        if (index >= 0 && index <= this.size()) {
            if (index == 0 && this.isSidechainingPresent()) {
                throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR + "Position 0 is occupied by the sidechaining.");
            }
            this.effects.add(index, u);
            if (index != 0) {
                this.connectEffects(this.effects.get(index - 1), u);
            } else {
                u.getGainIn().addInput(this.gainIn);
            }
            if (index != this.size() - 1) {
                this.connectEffects(u, this.effects.get(index + 1));
            } else {
                this.gainOut.clearInputConnections();
                this.gainOut.addInput(u.getGainOut());
            }
        } else {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

    private void connectEffects(final RPEffect from, final RPEffect to) {
        to.getGainIn().clearInputConnections();
        to.getGainIn().addInput(from.getGainOut());
    }

    /**
     * {@inheritDoc}
     * @param index a position in the sequence.
     * @throws IllegalStateException if there is only one {@link AudioElement} stored when this method is called.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    @Override
    public void removeEffectAtPosition(final int index) {
        if (index >= 0 && index <= this.size() - 1) {
            if (this.size() > 1) {
                if (index != this.size() - 1) {
                    this.getEffectAtPosition(index + 1).removeAllConnections(this.getEffectAtPosition(index));
                    if (index != 0) {
                        this.connectEffects(this.getEffectAtPosition(index - 1), this.getEffectAtPosition(index + 1));
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
    public void moveEffect(final int currentIndex, final int newIndex) {
        if (currentIndex >= 0
                && newIndex >= 0
                && currentIndex <= this.size() - 1
                && newIndex <= this.size() - 1
                && !(this.getEffectAtPosition(currentIndex) instanceof Sidechaining)
                && !(this.getEffectAtPosition(newIndex) instanceof Sidechaining)) {
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
    public void swapEffects(final int index1, final int index2) {
        if (index1 >= 0
                &&  index2 >= 0
                && index1 <= this.size() - 1
                && index2 <= this.size() - 1
                && !(this.getEffectAtPosition(index1) instanceof Sidechaining)
                && !(this.getEffectAtPosition(index2) instanceof Sidechaining)) {
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
    public void replace(final int index, final RPEffect u) {
        try {
            this.removeEffectAtPosition(index);
            this.addEffectAtPosition(u, index);
        } catch (IllegalStateException ex) {
            throw new IllegalArgumentException(ILLEGAL_INDEX_ERROR);
        }
    }

}
