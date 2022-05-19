package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import daw.core.mixer.RPMixer;
import daw.manager.ChannelLinker;

public abstract class AbstractJacksonDeserializer<T> implements Deserializer<T> {

    protected final ObjectMapper mapper;

    public AbstractJacksonDeserializer() {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public abstract T deserialize(String text);

}
