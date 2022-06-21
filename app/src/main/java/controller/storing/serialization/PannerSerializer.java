package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.beadsproject.beads.ugens.Panner;
import java.io.IOException;

// package protection as it is used only by the ManagerSerializer
class PannerSerializer extends StdSerializer<Panner> {

    private static final long serialVersionUID = -3209115649196199305L;

    protected PannerSerializer() {
        this(null);
    }

    protected PannerSerializer(final Class<Panner> t) {
        super(t);
    }

    @Override
    public void serialize(final Panner value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("position", value.getPos());
        gen.writeEndObject();
    }
}
