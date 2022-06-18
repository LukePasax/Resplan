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

    protected EffectSerializer() {
        this(null);
    }

    protected EffectSerializer(Class<RPEffect> t) {
        super(t);
    }

    @Override
    public void serialize(RPEffect value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        for (final var parameter: value.getParameters().entrySet()) {
            gen.writeNumberField(parameter.getKey(), parameter.getValue());
        }
        gen.writeNumberField("ins", value.getIns());
        gen.writeNumberField("outs", value.getOuts());
    }

    @Override
    public void serializeWithType(RPEffect value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
            throws IOException {
        WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        this.serialize(value, gen, provider);
        typeSer.writeTypeSuffix(gen, typeId);
    }

}
