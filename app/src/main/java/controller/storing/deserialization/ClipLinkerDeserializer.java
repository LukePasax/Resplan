package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.clip.RPClip;
import daw.manager.RPClipLinker;
import planning.RPPart;
import java.io.IOException;

public class ClipLinkerDeserializer extends JsonDeserializer<RPClipLinker> {

    @Override
    public RPClipLinker deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final var cl = new ObjectMapper().registerModule(new SimpleModule()
                .addDeserializer(RPClip.class, new ClipDeserializer())
                .addKeyDeserializer(RPPart.class, new PartKeyDeserializer())).readValue(p, RPClipLinker.class);
        return cl;
    }

}
