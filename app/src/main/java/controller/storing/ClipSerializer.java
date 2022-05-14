package controller.storing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.clip.EmptyClip;
import daw.core.clip.NoContent;
import daw.core.clip.RPClip;
import net.beadsproject.beads.data.Sample;
import java.io.File;
import java.io.IOException;

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
            gen.writeNumberField("content position", 0);
        } else {
            gen.writeNumberField("content position", value.getContentPosition());
        }
        if (value.getContent() instanceof Sample) {
            gen.writeStringField("content name",((Sample) value.getContent()).getFileName());
        } else if (value.getContent() instanceof File) {
            gen.writeStringField("content name", ((File) value.getContent()).getName());
        } else if (value.getContent() instanceof NoContent) {
            gen.writeStringField("content name", "");
        }
        gen.writeEndObject();
    }

}
