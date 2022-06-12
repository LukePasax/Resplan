package controller.storing.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import daw.core.audioprocessing.RPEffect;
import daw.core.audioprocessing.BasicSidechaining;
import daw.core.clip.RPClip;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

public abstract class AbstractJacksonSerializer<T> implements Serializer<T> {

    protected final ObjectMapper mapper;

    protected AbstractJacksonSerializer(boolean enabledGetters, boolean enableFields) {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .registerModule(new Jdk8Module())
                .registerModule(new SimpleModule()
                        .addSerializer(RPEffect.class, new EffectSerializer())
                        .addSerializer(Panner.class, new PannerSerializer())
                        .addSerializer(Gain.class, new GainSerializer())
                        .addSerializer(RPClip.class, new ClipSerializer()));
        if (!enabledGetters) {
            this.mapper.disable(MapperFeature.AUTO_DETECT_GETTERS).disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
        }
        if (!enableFields) {
            this.mapper.disable(MapperFeature.AUTO_DETECT_FIELDS);
        }
    }

    @Override
    public abstract String serialize(T element);

}
