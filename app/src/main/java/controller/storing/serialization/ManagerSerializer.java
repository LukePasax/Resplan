package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import daw.manager.Manager;

public class ManagerSerializer extends AbstractJacksonSerializer<Manager> {

    public ManagerSerializer(boolean enabledGetters, boolean enableFields) {
        super(enabledGetters, enableFields);
    }

    @Override
    public String serialize(Manager element) {
        final JsonNode node = mapper.valueToTree(element);
        final ObjectWriter ow = mapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        try {
            return ow.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Serialization of the given object of class " +
                    element.getClass() + " went wrong.");
        }
    }

}
