package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.clip.*;
import net.beadsproject.beads.data.Sample;
import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;

public class ClipSerializer extends StdSerializer<RPClip> {

    protected ClipSerializer() {
        this(null);
    }
    protected ClipSerializer(Class<RPClip> t) {
        super(t);
    }

    @Override
    public void serialize(RPClip value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value instanceof SampleClip) {
            gen.writeNumberField("content position", value.getContentPosition());
            gen.writeStringField("content name", ((Sample) value.getContent()).getFileName());
        }
        gen.writeNumberField("duration", value.getDuration());
    }

    @Override
    public void serializeWithType(RPClip value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
            throws IOException {
        WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        this.serialize(value, gen, provider);
        typeSer.writeTypeSuffix(gen, typeId);
    }

}
