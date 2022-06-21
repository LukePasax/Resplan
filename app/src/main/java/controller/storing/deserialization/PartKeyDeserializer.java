package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import planning.RPPart;
import planning.EffectsPart;
import planning.SpeechPart;
import planning.SoundtrackPart;
import java.util.List;

// package protection as it is used only by the ManagerDeserializer
class PartKeyDeserializer extends KeyDeserializer {

    @Override
    public RPPart deserializeKey(final String key, final DeserializationContext context) {
        final var values = this.extractValues(key);
        if ("empty".equals(values.get(1))) {
            if (this.getType(values.get(2)).equals(RPPart.PartType.EFFECTS)) {
                return new EffectsPart(values.get(0));
            } else if (this.getType(values.get(2)).equals(RPPart.PartType.SOUNDTRACK)) {
                return new SoundtrackPart(values.get(0));
            } else {
                return new SpeechPart(values.get(0));
            }
        } else {
            if (this.getType(values.get(2)).equals(RPPart.PartType.EFFECTS)) {
                return new EffectsPart(values.get(0), values.get(1));
            } else if (this.getType(values.get(2)).equals(RPPart.PartType.SOUNDTRACK)) {
                return new SoundtrackPart(values.get(0), values.get(1));
            } else {
                return new SpeechPart(values.get(0), values.get(1));
            }
        }
    }

    private List<String> extractValues(final String key) {
        final var strings = key.split(",");
        return List.of(strings[0].split("=")[1],
                strings[1].split("=")[1].split("\\.|\\[")[1].split("\\]")[0],
                strings[2].split("=")[1].split("\\]")[0]);
    }

    private RPPart.PartType getType(final String type) {
        if ("SPEECH".equals(type)) {
            return RPPart.PartType.SPEECH;
        } else if ("EFFECTS".equals(type)) {
            return RPPart.PartType.EFFECTS;
        } else {
            return RPPart.PartType.SOUNDTRACK;
        }
    }

}
