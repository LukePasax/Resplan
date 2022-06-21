package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.beadsproject.beads.ugens.Gain;
import java.io.IOException;

// package protection as it is used only by the ManagerSerializer
class GainSerializer extends StdSerializer<Gain> {

    private static final long serialVersionUID = 15039624L;

    protected GainSerializer() {
        this(null);
    }

    protected GainSerializer(final Class<Gain> t) {
        super(t);
    }

    @Override
    public void serialize(final Gain value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("gain value", value.getGain());
        gen.writeEndObject();
    }

}
