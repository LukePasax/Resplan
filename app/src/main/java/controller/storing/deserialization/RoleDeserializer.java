package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import java.io.IOException;

public class RoleDeserializer extends KeyDeserializer {

    /**
     * {@inheritDoc}
     * @param key
     * @param ctxt
     */
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return null;
    }
}
