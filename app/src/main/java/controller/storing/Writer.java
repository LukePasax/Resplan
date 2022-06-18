package controller.storing;

import java.io.IOException;

/**
 * Interface that can be used when the sole need is to write data onto a specific location, for example a file.
 * This is particularly useful in the context of serialization.
 * The use of this interface encapsulates the mean through which the location is written,
 * as it may vary with time or necessity. Furthermore, different implementations can provide different
 * ways to write for very specific instances, for example if the emphasis has to be put on efficiency.
 * Implementations' constructors must specify the location to write onto. This means that to write onto
 * two different locations, the client will need two different instances of the implementation.
 */
public interface Writer {

    /**
     * Generic method that writes data onto a location.
     * @return the data to be written.
     * @throws IOException if there are problems when writing.
     */
    void write(String data) throws IOException;

}
