package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.beadsproject.beads.ugens.Gain;
import java.io.IOException;

// package protection as it is used only by the ManagerSerializer
class GainSerializer extends StdSerializer<Gain> {

    protected GainSerializer() {
        this(null);
    }

    protected GainSerializer(Class<Gain> t) {
        super(t);
    }

    @Override
    public void serialize(Gain value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("gain value", value.getGain());
        gen.writeEndObject();
    }

}
