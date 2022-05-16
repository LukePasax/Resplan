package controller.storing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public abstract class JacksonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper mapper;

    public JacksonDeserializer() {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public abstract T deserialize(String text);

}
