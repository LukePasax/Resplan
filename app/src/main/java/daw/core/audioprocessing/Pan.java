package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

import java.util.Map;

public class Pan extends UGen implements AudioElement {

    private final Gain gainIn;
    private final Gain gainOut;
    private final Gain left;
    private final Gain right;
    private float value;

    public Pan() {
        super(AudioContextManager.getAudioContext());
        this.gainIn = new Gain(AudioContextManager.getAudioContext(), 2, 1.0f);
        this.gainOut = new Gain(AudioContextManager.getAudioContext(), 2, 1.0f);
        this.left = new Gain(AudioContextManager.getAudioContext(), 1, 1.0f);
        this.right = new Gain(AudioContextManager.getAudioContext(), 1, 1.0f);
        this.initializeStructure();
    }

    private void initializeStructure() {
        this.left.addInput(0, this.gainIn, 0);
        this.right.addInput(0, this.gainIn, 1);
        this.gainOut.addInput(0, this.left, 0);
        this.gainOut.addInput(1,this.right,0);
    }

    @Override
    public Map<String, Float> getParameters() {
        return Map.of("value", this.value);
    }

    @Override
    public void setParameters(Map<String, Float> parameters) {
        this.value = parameters.get("value");
        this.left.setGain((float) Math.cos(Math.PI * (1 + value) / 4));
        this.right.setGain((float) Math.sin(Math.PI * (1 + value) / 4));
    }

    @Override
    public int getIns() {
        return this.gainIn.getIns();
    }

    @Override
    public int getOuts() {
        return this.gainOut.getOuts();
    }

    @Override
    public Gain getGainIn() {
        return this.gainIn;
    }

    @Override
    public Gain getGainOut() {
        return this.gainOut;
    }

    @Override
    public void calculateBuffer() {
    }

}
