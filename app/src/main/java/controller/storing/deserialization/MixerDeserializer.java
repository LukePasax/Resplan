package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import daw.core.mixer.Mixer;
import daw.core.mixer.RPMixer;
import java.io.IOException;

public class MixerDeserializer extends StdDeserializer<RPMixer> {

    protected MixerDeserializer() {
        this(null);
    }

    protected MixerDeserializer(Class<Mixer> vc) {
        super(vc);
    }

    @Override
    public RPMixer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        final var man = new ObjectMapper().readValue(p, Mixer.class);
        return man;
    }

}
