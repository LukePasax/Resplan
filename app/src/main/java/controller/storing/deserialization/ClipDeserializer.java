package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import daw.core.clip.EmptyClip;
import daw.core.clip.RPClip;
import daw.core.clip.SampleClip;
import java.io.IOException;

public class ClipDeserializer extends JsonDeserializer<RPClip> {

    @Override
    public RPClip deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new ObjectMapper().readValue(p, checkInstance(p));
    }

    private Class<? extends RPClip> checkInstance(JsonParser p) throws IOException {
        final var string = p.getCodec().readTree(p).get("type").toString();
        if (string.equals("\"empty clip\"")) {
            return EmptyClip.class;
        } else {
            return SampleClip.class;
        }
    }

}
