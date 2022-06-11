package controller.storing.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import daw.core.channel.RPChannel;
import java.io.IOException;

public class ChannelDeserializer extends JsonDeserializer<RPChannel> {

    @Override
    public RPChannel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return new ObjectMapper().readValue(p, RPChannel.class);
    }

}
