package controller.storing.deserialization;

/**
 * General interface for deserialization of objects.
 * @param <T> the type of the deserialized object.
 */
public interface Deserializer<T> {

    /**
     * Deserialize the given data to retrieve an object.
     * @param text the textual data to be deserialized.
     * @return a deserialized object.
     */
    T deserialize(String text);

}
