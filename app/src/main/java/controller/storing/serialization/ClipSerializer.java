package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.clip.RPClip;
import daw.core.clip.SampleClip;
import net.beadsproject.beads.data.Sample;
import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;

// package protection as it is used only by the ManagerSerializer
class ClipSerializer extends StdSerializer<RPClip> {

    private static final long serialVersionUID = 3787228129180295107L;

    protected ClipSerializer() {
        this(null);
    }
    protected ClipSerializer(final Class<RPClip> t) {
        super(t);
    }

    @Override
    public void serialize(final RPClip value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        if (value instanceof SampleClip) {
            gen.writeNumberField("content position", value.getContentPosition());
            gen.writeStringField("content name", ((Sample) value.getContent()).getFileName());
        }
        gen.writeNumberField("duration", value.getDuration());
        gen.writeStringField("name", value.getTitle());
    }

    @Override
    public void serializeWithType(final RPClip value, final JsonGenerator gen, final SerializerProvider provider,
                                  final TypeSerializer typeSer) throws IOException {
        final WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        this.serialize(value, gen, provider);
        typeSer.writeTypeSuffix(gen, typeId);
    }

}
