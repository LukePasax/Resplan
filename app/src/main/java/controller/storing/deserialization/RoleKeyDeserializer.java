package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import planning.EffectsRole;
import planning.RPRole;
import planning.SoundtrackRole;
import planning.SpeechRole;
import java.util.List;

/**
 * Key deserializer for {@link planning.RPRole.RoleType}.
 * Key deserializers are used for the deserialization of JSON content field names into Java Map keys.
 */
public class RoleKeyDeserializer extends KeyDeserializer {

    /**
     * {@inheritDoc}
     * @param key the string representation of a part.
     * @return an {@link RPRole}.
     */
    @Override
    public RPRole deserializeKey(String key, DeserializationContext context) {
        final var values = this.extractValues(key);
        if (values.get(1).equals("empty")) {
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

    private List<String> extractValues(String key) {
        final var strings = key.split(",");
        return List.of(strings[0].split("=")[1],
                strings[1].split("=")[1].split("\\.|\\[")[1].split("\\]")[0],
                strings[2].split("=")[1].split("\\]")[0]);
    }

    private RPRole.RoleType getType(String type) {
        if (type.equals("SPEECH")) {
            return RPRole.RoleType.SPEECH;
        } else if (type.equals("EFFECTS")) {
            return RPRole.RoleType.EFFECTS;
        } else {
            return RPRole.RoleType.SOUNDTRACK;
        }
    }

}
