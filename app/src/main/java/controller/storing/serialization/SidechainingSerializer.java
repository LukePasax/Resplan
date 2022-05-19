package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.audioprocessing.BasicSidechaining;
import java.io.IOException;

public class SidechainingSerializer extends StdSerializer<BasicSidechaining> {

    protected SidechainingSerializer() {
        this(null);
    }

    protected SidechainingSerializer(Class<BasicSidechaining> t) {
        super(t);
    }

    @Override
    public void serialize(BasicSidechaining value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (final var parameter: value.getParameters().entrySet()) {
            gen.writeNumberField(parameter.getKey(), parameter.getValue());
        }
        gen.writeNumberField("ins", value.getIns());
        gen.writeNumberField("outs", value.getOuts());
        gen.writeEndObject();
    }

}
