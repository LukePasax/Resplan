package daw.core.audioprocessing;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import daw.core.channel.RPChannel;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import java.util.List;

/**
 * Manages the sequence of effects that manipulate an audio source.
 * A ProcessingUnit instantiation must always contain at least one effect.
 * It is important to note that the order the effects are stored by is relevant to the processing
 * and ultimately to the output of the unit.
 */
@JsonDeserialize(as = BasicProcessingUnit.class)
public interface ProcessingUnit {

    /**
     * Adds the audio signal that needs to be processed.
     * @param g the {@link Gain} that represents the audio source.
     */
    void addInput(Gain g);

    /**
     * Allows the client to receive the output of the whole processing.
     * @param u the {@link UGen} the UGen to which the output must be connected to.
     */
    void connect(UGen u);

    /**
     * Makes the processing unit sidechained to a specific source.
     * To know what sidechaining is and when to use it, please read the documentation at {@link Sidechaining}.
     * Note that if the processing unit is already sidechained this method does nothing, as a processing
     * unit cannot have two different sidechained sources at the same time. If the desired action is to change
     * the source of the sidechaining, first call removeSidechaining and then this method.
     * @param s the compressor that performs the sidechaining.
     */
    void addSidechaining(SidechainingImpl s);

    /**
     * Makes the processing unit not be sidechained to any external source.
     */
    void removeSidechaining();

    /**
     * Allows knowing whether the {@link ProcessingUnit} (and ultimately the {@link RPChannel} it is
     * connected to) is sidechained or not.
     * @return true if the {@link ProcessingUnit} is sidechained.
     */
    boolean isSidechainingPresent();

    /**
     * Allows getting the sequence of effects as they are stored when this method is called.
     * @return the current {@link List} of effects.
     */
    List<RPEffect> getEffects();

    /**
     * Allows finding which effect is stored at the given position of the sequence.
     * @param index a position in the sequence.
     * @return the {@link RPEffect} that is placed at the given position.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    RPEffect getEffectAtPosition(int index);

    /**
     * Gets the amount of {@link RPEffect}s currently stored in the processing unit.
     * @return the number of effects.
     */
    int size();

    /**
     * Adds a new effect at the last position of the sequence.
     * Note that this method also matches the preceding effect output to the given effect input.
     * If the number of outputs of the preceding effect is greater than the number of inputs of the given
     * effect then the extra outputs are not connected.
     * If the number of inputs of the given effect is greater than the number of outputs of the preceding
     * effect then the outputs are cycled to fill all inputs.
     * @param u the {@link RPEffect} that represents the effect to be added.
     * @throws IllegalArgumentException if the given position is out of bounds or if the given effect
     * is an instantiation of {@link Sidechaining}.
     */
    void addEffect(RPEffect u);

    /**
     * Adds a new effect in the given position of the sequence. If the position is the first out-of-bound
     * index to the right this method has the exact same behavior of the addEffect method.
     * For example, this occurs when the sequence's last element is at index 3 and the given position is index 4.
     * Note that this method also match the preceding effect output (if present) to the given effect input
     * and the output of the latter to the following effect input (if present).
     * The same policies of the addEffect method are used when the number of inputs and outputs of
     * two consecutive {@link RPEffect} is different.
     * @param u the {@link RPEffect} that represents the effect to be added.
     * @param index a position in the sequence.
     * @throws IllegalArgumentException if the given position is out of bounds or if the given effect
     * is an instantiation of {@link Sidechaining}.
     */
    void addEffectAtPosition(RPEffect u, int index);

    /**
     * Removes the effect at the given position from the sequence.
     * @param index a position in the sequence.
     * @throws IllegalStateException if there is only one {@link AudioElement} stored when this method is called.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    void removeEffectAtPosition(int index);

    /**
     * Moves the effect at the given position to a new position and shifts the other effects accordingly.
     * If the newIndex position is out of bounds for the current sequence, this method does nothing.
     * @param currentIndex the position of the effect to be moved.
     * @param newIndex the position to which the effect must be moved.
     * @throws IllegalArgumentException if one or both of the given positions are out of bounds.
     */
    void moveEffect(int currentIndex, int newIndex);

    /**
     * Swaps two effects, while leaving all the others at their current position.
     * @param index1 the position of an effect that must be swapped.
     * @param index2 the position of the other effect that must be swapped.
     * @throws IllegalArgumentException if one or both of the given positions are out of bounds.
     */
    void swapEffects(int index1, int index2);

    /**
     * Replaces the effect at a certain position of the sequence with the given effect.
     * @param index the index of the effect that must be replaced.
     * @param u the effect that serves as a replacement.
     * @throws IllegalArgumentException if the given position is out of bounds.
     */
    void replace(int index, RPEffect u);

}
