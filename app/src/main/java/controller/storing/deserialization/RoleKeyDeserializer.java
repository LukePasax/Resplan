package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import planning.EffectsRole;
import planning.RPRole;
import planning.SoundtrackRole;
import planning.SpeechRole;
import java.util.List;

// package protection as it is used only by the ManagerDeserializer
class RoleKeyDeserializer extends KeyDeserializer {

    @Override
    public RPRole deserializeKey(final String key, final DeserializationContext context) {
        final var values = this.extractValues(key);
        if ("empty".equals(values.get(1))) {
            if (this.getType(values.get(2)).equals(RPRole.RoleType.EFFECTS)) {
                return new EffectsRole(values.get(0));
            } else if (this.getType(values.get(2)).equals(RPRole.RoleType.SOUNDTRACK)) {
                return new SoundtrackRole(values.get(0));
            } else {
                return new SpeechRole(values.get(0));
            }
        } else {
            if (this.getType(values.get(2)).equals(RPRole.RoleType.EFFECTS)) {
                return new EffectsRole(values.get(0), values.get(1));
            } else if (this.getType(values.get(2)).equals(RPRole.RoleType.SOUNDTRACK)) {
                return new SoundtrackRole(values.get(0), values.get(1));
            } else {
                return new SpeechRole(values.get(0), values.get(1));
            }
        }
    }

    private List<String> extractValues(final String key) {
        final var strings = key.split(",");
        return List.of(strings[0].split("=")[1],
                strings[1].split("=")[1].split("\\.|\\[")[1].split("\\]")[0],
                strings[2].split("=")[1].split("\\]")[0]);
    }

    private RPRole.RoleType getType(final String type) {
        if ("SPEECH".equals(type)) {
            return RPRole.RoleType.SPEECH;
        } else if ("EFFECTS".equals(type)) {
            return RPRole.RoleType.EFFECTS;
        } else {
            return RPRole.RoleType.SOUNDTRACK;
        }
    }

}
