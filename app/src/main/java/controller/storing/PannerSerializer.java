package controller.storing;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.beadsproject.beads.ugens.Panner;
import java.io.IOException;

public class PannerSerializer extends StdSerializer<Panner> {

    protected PannerSerializer() {
        this(null);
    }

    protected PannerSerializer(Class<Panner> t) {
        super(t);
    }

    @Override
    public void serialize(Panner value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("position", value.getPos());
        gen.writeEndObject();
    }
}
