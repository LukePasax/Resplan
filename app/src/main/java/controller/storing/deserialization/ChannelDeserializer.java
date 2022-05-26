package controller.storing.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import daw.core.channel.BasicChannelFactory;
import daw.core.channel.RPChannel;
import java.io.IOException;

public class ChannelDeserializer extends JsonDeserializer<RPChannel> {

    @Override
    public RPChannel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        final RPChannel channel;
        JsonNode node = p.getCodec().readTree(p);
        System.out.println(node.asText());
        /*
        if (type == "AUDIO") {
            channel = new BasicChannelFactory().basic();
        } else if (type == "RETURN") {
            channel = new BasicChannelFactory().returnChannel();
        } else {
            channel = new BasicChannelFactory().masterChannel();
        }
        final boolean isEnabled = node.get(1).asBoolean();
        if (!isEnabled) {
            channel.disable();
        }
        return channel;
        */
        return new BasicChannelFactory().basic();
    }

}
