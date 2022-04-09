package daw.core.channel;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import java.util.List;
import java.util.Optional;

/**
 * Manages the sequence of effects that manipulate an audio source.
 * A ProcessingUnit instantiation must always contain at least one effect.
 * It is important to note that the order the effects are stored by is relevant to the processing
 * and ultimately to the output of the unit.
 */
public interface ProcessingUnit {

    /**
     * Adds the audio that must be processed.
     * @param g the source.
     */
    void addInput(Gain g);

    /**
     * Allows the client to receive the output of the whole processing.
     * @param u the {@link UGen} the UGen to which the output must be connected to.
     */
    void connect(UGen u);

    /**
     *
     * @return the current {@link List} of effects.
     */
    List<UGen> getEffects();

    /**
     * Allows finding which effect is stored in a certain position of the sequence.
     * @param index a position in the sequence.
     * @return the effect that is placed at the given index.
     */
    UGen getEffectAtPosition(int index);

    /**
     * Adds a new effect in the last position of the sequence.
     * Note that this method also match the preceding effect output to the given effect input.
     * @param u the {@link UGen} that represents the effect to be added.
     */
    void addEffect(UGen u);

    /**
     * Adds a new effect in the given position of the sequence. If the position is out of bounds
     * for the current sequence, this method does nothing. The only exception is the first out-of-bound
     * index to the right, for example if the sequence's last element is at index 3 and the given position is index 4.
     * In this case, this method has the exact same behavior of the addEffect method.
     * Note that this method also match the preceding effect output (if present) to the given effect input
     * and the output of the latter to the following effect input (if present).
     * @param u the {@link UGen} that represents the effect to be added.
     * @param index a position in the sequence.
     */
    void addEffectAtPosition(UGen u, int index);

    /**
     * Removes the effect at the given position from the sequence.
     * This operation cannot be performed if there is only one effect stored.
     * @param index a position in the sequence.
     */
    void removeEffectAtPosition(int index);

    /**
     * Moves the effect at the given position to a new position and shifts the other effects accordingly.
     * If the newIndex position is out of bounds for the current sequence, this method does nothing.
     * @param currentIndex the position of the effect to be moved.
     * @param newIndex the position to which the effect must be moved.
     */
    void moveEffect(int currentIndex, int newIndex);

    /**
     * Swaps two effects, while maintaining every other effect intact.
     * @param index1 the position of an effect that must be swapped.
     * @param index2 the position of the other effect that must be swapped.
     */
    void swapEffects(int index1, int index2);

    /**
     * Replaces the effect at a certain position of the sequence with the given effect.
     * @param index the index of the effect that must be replaced.
     * @param u the effect that serves as a replacement.
     */
    void replace(int index, UGen u);

}
