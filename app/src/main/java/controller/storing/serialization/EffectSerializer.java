package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.audioprocessing.RPEffect;
import java.io.IOException;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;

// package protection as it is used only by the ManagerSerializer
class EffectSerializer extends StdSerializer<RPEffect> {

    private static final long serialVersionUID = 40039632L;

    protected EffectSerializer() {
        this(null);
    }

    protected EffectSerializer(final Class<RPEffect> t) {
        super(t);
    }

    @Override
    public void serialize(final RPEffect value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        for (final var parameter: value.getParameters().entrySet()) {
            gen.writeNumberField(parameter.getKey(), parameter.getValue());
        }
        gen.writeNumberField("ins", value.getIns());
        gen.writeNumberField("outs", value.getOuts());
    }

    @Override
    public void serializeWithType(final RPEffect value, final JsonGenerator gen, final SerializerProvider provider,
                                  final TypeSerializer typeSer) throws IOException {
        final WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        this.serialize(value, gen, provider);
        typeSer.writeTypeSuffix(gen, typeId);
    }

}
