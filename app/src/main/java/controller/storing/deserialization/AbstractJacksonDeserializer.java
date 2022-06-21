package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;

/**
 * Abstract class for Jackson deserialization. Jackson is the go-to library for JSON
 * serialization and deserialization in Java software development.
 * @param <T> {@inheritDoc}
 */
public abstract class AbstractJacksonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper mapper;

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
     * Gets the Jackson mapper used for this deserializer.
     * @return an {@link ObjectMapper}.
     */
    protected ObjectMapper getMapper() {
        return this.mapper;
    }

    /**
     * {@inheritDoc}
     * @param text the textual data to be deserialized.
     * @return {@inheritDoc}
     */
    @Override
    public abstract T deserialize(String text) throws IOException;

}
