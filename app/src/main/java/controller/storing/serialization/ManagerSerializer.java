package controller.storing.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.audioprocessing.RPEffect;
import daw.core.clip.RPClip;
import daw.manager.Manager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

public class ManagerSerializer extends AbstractJacksonSerializer<Manager> {

    public ManagerSerializer() {
        this.mapper.registerModule(new SimpleModule()
                .addSerializer(RPEffect.class, new EffectSerializer())
                .addSerializer(Panner.class, new PannerSerializer())
                .addSerializer(Gain.class, new GainSerializer())
                .addSerializer(RPClip.class, new ClipSerializer()));
    }

    @Override
    public String serialize(Manager element) {
        final JsonNode node = mapper.valueToTree(element);
        final ObjectWriter ow = mapper.writer().with(SerializationFeature.INDENT_OUTPUT);
        try {
            return ow.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("Serialization of the given object of class " +
                    element.getClass() + " went wrong.");
        }
    }

}
