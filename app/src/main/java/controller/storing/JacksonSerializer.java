package controller.storing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class JacksonSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper;

    public JacksonSerializer() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public String serialize(T element) {
        final JsonNode node = mapper.valueToTree(element);
        final ObjectWriter ow = mapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        try {
            return ow.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Serialization of the given object of class " + element.getClass() +
                    " went wrong.");
        }
    }

}
