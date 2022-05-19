package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.mixer.RPMixer;
import daw.manager.ChannelLinker;

public abstract class JacksonDeserializer<T> implements Deserializer<T> {

    protected final ObjectMapper mapper;

    public JacksonDeserializer() {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new SimpleModule()
                        .addDeserializer(RPMixer.class, new MixerDeserializer())
                        .addDeserializer(ChannelLinker.class, new ChannelLinkerDeserializer()));
    }

    @Override
    public abstract T deserialize(String text);

}
