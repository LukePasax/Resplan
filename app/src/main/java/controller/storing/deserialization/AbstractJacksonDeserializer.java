package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Abstract class for Jackson deserialization. Jackson is the go-to library for JSON
 * serialization and deserialization in Java software development.
 * @param <T> {@inheritDoc}
 */
public abstract class AbstractJacksonDeserializer<T> implements Deserializer<T> {

    protected final ObjectMapper mapper;

    /**
     * Constructor of a general purpose Jackson deserializer.
     */
    protected AbstractJacksonDeserializer() {
        this.mapper = JsonMapper.builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module());
    }

    /**
     * Deserialize the given JSON data to retrieve an object.
     * @param text the textual data to be deserialized.
     * @return a deserialized object.
     */
    @Override
    public abstract T deserialize(String text);

}
