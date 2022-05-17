package controller.storing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.mixer.RPMixer;

public abstract class JacksonDeserializer<T> implements Deserializer<T> {

    protected final ObjectMapper mapper;

    public JacksonDeserializer() {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new SimpleModule().addDeserializer(RPMixer.class, new MixerDeserializer()));
    }

    @Override
    public abstract T deserialize(String text);

}
