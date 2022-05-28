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
        gen.writeNumberField("duration", value.getDuration());
        if (value instanceof EmptyClip) {
            gen.writeNumberField("content position", -1.0);
            gen.writeStringField("content name", "");
        } else if (value instanceof SampleClip) {
            gen.writeNumberField("content position", value.getContentPosition());
            gen.writeStringField("content name", ((Sample) value.getContent()).getSimpleName());
        } else if (value instanceof FileClip) {
            gen.writeNumberField("content position", value.getContentPosition());
            gen.writeStringField("content name", ((File) value.getContent()).getName());
        }
        gen.writeEndObject();
    }

}
