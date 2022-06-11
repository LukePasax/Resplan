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
        Class<? extends RPClip> instanceClass = null;
        if (this.checkInstance(p).equals(EmptyClip.class)) {
            instanceClass = EmptyClip.class;
        } else if (this.checkInstance(p).equals(SampleClip.class)) {
            instanceClass = SampleClip.class;
        }
        return new ObjectMapper().readValue(p, instanceClass);
    }

    private Class<? extends RPClip> checkInstance(JsonParser p) throws IOException {
        return RPClip.class;
    }

}
