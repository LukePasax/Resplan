package controller.storing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.audioprocessing.Sidechaining;
import java.io.IOException;

public class SidechainingSerializer extends StdSerializer<Sidechaining> {

    protected SidechainingSerializer() {
        this(null);
    }

    protected SidechainingSerializer(Class<Sidechaining> t) {
        super(t);
    }

    @Override
    public void serialize(Sidechaining value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (final var parameter: value.getParameters().entrySet()) {
            gen.writeNumberField(parameter.getKey(), parameter.getValue());
        }
        gen.writeNumberField("ins", value.getIns());
        gen.writeNumberField("outs", value.getOuts());
        gen.writeEndObject();
    }

}
