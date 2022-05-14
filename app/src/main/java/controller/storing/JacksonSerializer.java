package controller.storing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import daw.core.audioprocessing.RPEffect;
import daw.core.audioprocessing.Sidechaining;
import daw.core.clip.RPClip;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

public class JacksonSerializer<T> implements Serializer<T> {

    private final ObjectMapper mapper;

    public JacksonSerializer(boolean enabledGetters, boolean enableFields) {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .registerModule(new Jdk8Module())
                .registerModule(new SimpleModule()
                        .addSerializer(RPEffect.class, new EffectSerializer())
                        .addSerializer(Panner.class, new PannerSerializer())
                        .addSerializer(Gain.class, new GainSerializer())
                        .addSerializer(RPClip.class, new ClipSerializer())
                        .addSerializer(Sidechaining.class, new SidechainingSerializer()));
        if (!enabledGetters) {
            this.mapper.disable(MapperFeature.AUTO_DETECT_GETTERS).disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
        }
        if (!enableFields) {
            this.mapper.disable(MapperFeature.AUTO_DETECT_FIELDS);
        }
    }

    @Override
    public String serialize(T element) {
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
