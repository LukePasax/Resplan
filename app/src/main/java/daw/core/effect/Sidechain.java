package daw.core.effect;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.ugens.Compressor;

public class Sidechain extends UGen implements DataBeadReceiver {

    private Compressor compressor;

    public Sidechain(float threshold, float attack, float decay, float ratio, UGen sidechained) {
        super(AudioContextManager.getAudioContext());
        this.compressor = new Compressor(AudioContext.getDefaultContext(), 2, sidechained)
                .setThreshold(threshold).setAttack(attack).setDecay(decay).setRatio(ratio);
    }

    @Override
    public void calculateBuffer() {
        this.compressor.calculateBuffer();
    }

    @Override
    public DataBeadReceiver sendData(DataBead db) {
        return this.compressor.sendData(db);
    }

}
