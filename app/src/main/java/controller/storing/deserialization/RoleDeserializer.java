package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import planning.RPRole;
import java.io.IOException;

public class RoleDeserializer extends KeyDeserializer {

    @Override
    public RPRole deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return new ObjectMapper().readValue(key, RPRole.class);
    }

}
