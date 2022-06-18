package controller.storing.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public abstract class AbstractJacksonSerializer<T> implements Serializer<T> {

    protected final ObjectMapper mapper;

    protected AbstractJacksonSerializer() {
        this.mapper = JsonMapper.builder().disable(MapperFeature.AUTO_DETECT_FIELDS)
                .build()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .registerModule(new Jdk8Module());
    }

    @Override
    public abstract String serialize(T element);

}
