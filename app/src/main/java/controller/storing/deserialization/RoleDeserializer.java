package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import planning.EffectsRole;
import planning.RPRole;
import planning.SoundtrackRole;

import java.io.IOException;
import java.util.List;

public class RoleDeserializer extends KeyDeserializer {

    @Override
    public RPRole deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        key = key.split("\\[")[1];
        final var values = this.extractValues(key);
        if (this.getType(values.get(2)) == RPRole.RoleType.EFFECTS) {
            return new EffectsRole(values.get(0));
        } else if (this.getType(values.get(2)) == RPRole.RoleType.SOUNDTRACK) {
            return new SoundtrackRole(values.get(0));
        } else {
            return new EffectsRole(values.get(0));
        }
    }

    private List<String> extractValues(String key) {
        final var strings = key.split("=");
        return List.of(strings[1].split(",")[0], strings[2].split(",")[0], strings[3].split("\\]")[0]);
    }

    private final RPRole.RoleType getType(String type) {
        if (type == "SPEECH") {
            return RPRole.RoleType.SPEECH;
        } else if (type == "EFFECTS") {
            return RPRole.RoleType.EFFECTS;
        } else {
            return RPRole.RoleType.SOUNDTRACK;
        }
    }

}
