package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import daw.core.audioprocessing.Pan;
import java.io.IOException;

// package protection as it is used only by the ManagerSerializer
class PanSerializer extends StdSerializer<Pan> {

    private static final long serialVersionUID = -3209115649196199305L;

    protected PanSerializer() {
        this(null);
    }

    protected PanSerializer(final Class<Pan> t) {
        super(t);
    }

    @Override
    public void serialize(final Pan value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("position", value.getParameters().get("value"));
        gen.writeEndObject();
    }
}
