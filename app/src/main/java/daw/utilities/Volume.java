package daw.utilities;

/**
 * This interface models the concept of volume within a scale, that is the volume has a lower bound of 0
 * and an upper bound (TO DO: make the client select the upper bound).
 */
public interface Volume {

    /**
     * Sets the volume. If the parameter is out of the scale, this method does nothing.
     * @param vol the volume used as input.
     */
    void setVolume(int vol);

    /**
     * Increases the volume by 1 unity, but only if it does not put the volume out of scale.
     */
    void increment();

    /**
     * Decreases the volume by 1 unity, but only if it does not put the volume out of scale.
     */
    void decrement();

    /**
     * Returns the current numerical value of the volume.
     */
    int getVolume();

}
