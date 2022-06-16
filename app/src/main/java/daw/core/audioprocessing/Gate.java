package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.data.DataBead;
import java.util.Map;

public class Gate extends RPEffect {

    private final GateEffect gate;

    public Gate(@JsonProperty("ins") int channels) {
        super(channels);
        this.gate = new GateEffect(AudioContextManager.getAudioContext(), channels);
        this.gate.addInput(this.getGainIn());
        this.getGainOut().addInput(this.gate);
    }

    @Override
    public Map<String, Float> getParameters() {
        return Map.of("threshold", this.gate.getThreshold(), "ratio", this.gate.getRatio(),
                "attack", this.gate.getAttack(), "decay", this.gate.getDecay(),
                "current compression", this.gate.getCurrentCompression());
    }

    @Override
    public void setParameters(Map<String, Float> parameters) {
        final DataBead db = new DataBead();
        db.putAll(parameters);
        this.gate.sendData(db);
    }

    @Override
    public void calculateBuffer() {
        this.gate.calculateBuffer();
    }

}
