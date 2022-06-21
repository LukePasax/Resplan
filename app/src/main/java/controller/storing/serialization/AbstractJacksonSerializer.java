package controller.storing.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;

/**
 * Abstract class for Jackson serialization. Jackson is the go-to library for JSON
 * serialization and deserialization in Java software development.
 * @param <T> {@inheritDoc}
 */
public abstract class AbstractJacksonSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper;

    /**
     * Constructor of a general purpose Jackson serializer.
     */
    protected AbstractJacksonSerializer() {
        this.mapper = JsonMapper.builder().disable(MapperFeature.AUTO_DETECT_FIELDS)
                .build()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .registerModule(new Jdk8Module());
    }


    /**
     * Gets the Jackson mapper used for this serializer.
     * @return an {@link ObjectMapper}.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * {@inheritDoc}
     * @param element the object to be serialized.
     * @return {@inheritDoc}
     */
    @Override
    public abstract String serialize(T element) throws IOException;

}
