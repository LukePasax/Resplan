package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import daw.core.channel.RPChannel;
import daw.core.clip.RPClip;
import daw.manager.RPChannelLinker;
import planning.RPPart;
import planning.RPRole;

public abstract class AbstractJacksonDeserializer<T> implements Deserializer<T> {

    protected final ObjectMapper mapper;

    protected AbstractJacksonDeserializer() {
        this.mapper = new JsonMapper().builder()
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new Jdk8Module())
                .registerModule(new SimpleModule()
                        .addDeserializer(RPChannel.class, new ChannelDeserializer())
                        .addDeserializer(RPChannelLinker.class, new ChannelLinkerDeserializer())
                        .addDeserializer(RPClip.class, new ClipDeserializer())
                        .addKeyDeserializer(RPRole.class, new RoleKeyDeserializer())
                        .addKeyDeserializer(RPPart.class, new PartKeyDeserializer()));
    }

    @Override
    public abstract T deserialize(String text);

}
