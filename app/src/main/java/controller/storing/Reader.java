package controller.storing;

import java.io.IOException;

/**
 * Interface that can be used when the sole need is to read all data from a source, for example a file.
 * This is particularly useful in the context of deserialization.
 * The use of this interface encapsulates the mean through which the source is read,
 * as it may vary with time or necessity. Furthermore, different implementations can provide different
 * ways to read for very specific instances, for example if the emphasis has to be put on efficiency.
 * Implementations' constructors must specify the source to read from. This means that to read from
 * two different sources, the client will need two different instances of the implementation.
 */
public interface Reader {

    /**
     * Generic method that gets a string representation the whole data content of a source.
     * @return the data read from a source.
     * @throws IOException if there are problems when reading.
     */
    String read() throws IOException;

}
