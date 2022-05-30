package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.clip.*;
import net.beadsproject.beads.data.Sample;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class ClipSerializer extends StdSerializer<RPClip> {

    protected ClipSerializer() {
        this(null);
    }
    protected ClipSerializer(Class<RPClip> t) {
        super(t);
    }

    @Override
    public void serialize(RPClip value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value instanceof EmptyClip) {
            gen.writeStringField("type name", "empty");
        } else if (value instanceof SampleClip) {
            gen.writeStringField("type name", "sample");
            gen.writeNumberField("content position", value.getContentPosition());
            gen.writeObjectField("content name", new File(((Sample) value.getContent()).getFileName()));
        }
        gen.writeNumberField("duration", value.getDuration());
        gen.writeEndObject();
    }

}
