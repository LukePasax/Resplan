package controller.storing.serialization;

import java.io.IOException;

/**
 * General interface for serialization of objects.
 * @param <T> the type of the serialized object.
 */
public interface Serializer<T> {

    /**
     * Serialize the given object to JSON data.
     * @param element the object to be serialized.
     * @return the textual data representing the given object.
     */
    String serialize(T element) throws IOException;

}
