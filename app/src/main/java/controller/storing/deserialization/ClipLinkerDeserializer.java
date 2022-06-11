package controller.storing.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.clip.RPClip;
import daw.manager.ClipLinker;
import daw.manager.RPClipLinker;
import planning.RPPart;
import java.io.IOException;

public class ClipLinkerDeserializer extends JsonDeserializer<RPClipLinker> {

    @Override
    public RPClipLinker deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final JsonNode node = p.getCodec().readTree(p);
        System.out.println(node.get("clipMap").asText());
        return null;
    }

}
