package controller.storing.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import daw.manager.ChannelLinker;
import java.io.IOException;

public class ChannelLinkerDeserializer extends StdDeserializer<ChannelLinker> {

    protected ChannelLinkerDeserializer() {
        this(null);
    }

    protected ChannelLinkerDeserializer(Class<?> vc) {
        super(vc);
    }

    /**
     * {@inheritDoc}
     * @param p    Parsed used for reading JSON content
     * @param ctxt Context that can be used to access information about
     *             this deserialization activity.
     * @return Deserialized value
     */
    @Override
    public ChannelLinker deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        final var cLinker = new ObjectMapper().readValue(p, ChannelLinker.class);
        return cLinker;
    }

}
