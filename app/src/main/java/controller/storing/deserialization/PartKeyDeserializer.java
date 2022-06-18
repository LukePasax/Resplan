package controller.storing.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import planning.*;
import java.util.List;

// package protection as it is used only by the ManagerDeserializer
class PartKeyDeserializer extends KeyDeserializer {

    @Override
    public RPPart deserializeKey(String key, DeserializationContext context) {
        final var values = this.extractValues(key);
        if (values.get(1).equals("empty")) {
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

    private List<String> extractValues(String key) {
        final var strings = key.split(",");
        return List.of(strings[0].split("=")[1],
                strings[1].split("=")[1].split("\\.|\\[")[1].split("\\]")[0],
                strings[2].split("=")[1].split("\\]")[0]);
    }

    private RPPart.PartType getType(String type) {
        if (type.equals("SPEECH")) {
            return RPPart.PartType.SPEECH;
        } else if (type.equals("EFFECTS")) {
            return RPPart.PartType.EFFECTS;
        } else {
            return RPPart.PartType.SOUNDTRACK;
        }
    }

}
