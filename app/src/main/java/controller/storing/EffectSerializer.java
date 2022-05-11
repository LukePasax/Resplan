package controller.storing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.audioprocessing.RPEffect;
import java.io.IOException;

public class EffectSerializer extends StdSerializer<RPEffect> {

    protected EffectSerializer() {
        this(null);
    }

    protected EffectSerializer(Class<RPEffect> t) {
        super(t);
    }

    @Override
    public void serialize(RPEffect value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (final var parameter: value.getParameters().entrySet()) {
            gen.writeNumberField(parameter.getKey(), parameter.getValue());
        }
        gen.writeNumberField("ins", value.getIns());
        gen.writeNumberField("outs", value.getOuts());
    }

}
