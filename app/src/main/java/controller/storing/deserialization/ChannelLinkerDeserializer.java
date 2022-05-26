package controller.storing.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import daw.manager.ChannelLinker;
import java.io.IOException;

public class ChannelLinkerDeserializer extends JsonDeserializer<ChannelLinker> {

    @Override
    public ChannelLinker deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return null;
    }

}
