package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.ugens.Compressor;

public class Gate extends RPEffect {

    private Compressor compressor;

    public Gate(float threshold, float attack, float decay, float ratio) {
        super(AudioContextManager.getAudioContext(), 1, 1);
        this.compressor = new Compressor(AudioContextManager.getAudioContext()).setThreshold(threshold).
                setAttack(attack).setDecay(decay).setRatio(ratio);
    }

    @Override
    public void calculateBuffer() {
        // TO DO
    }

    @Override
    public DataBeadReceiver sendData(DataBead db) {
        return this.compressor.sendData(db);
    }

}
