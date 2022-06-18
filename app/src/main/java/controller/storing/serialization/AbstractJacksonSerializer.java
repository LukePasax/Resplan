package controller.storing.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * Abstract class for Jackson serialization. Jackson is the go-to library for JSON
 * serialization and deserialization in Java software development.
 * @param <T> {@inheritDoc}
 */
public abstract class AbstractJacksonSerializer<T> implements Serializer<T> {

    protected final ObjectMapper mapper;

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
     * {@inheritDoc}
     * @param element the object to be serialized.
     * @return {@inheritDoc}
     */
    @Override
    public abstract String serialize(T element);

}
