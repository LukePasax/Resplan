package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.ugens.Compressor;

/**
 * This class represents a particular implementation of compressor that supports the sidechain technique.
 * Sidechaining is a compression technique that makes the sound coming in from a source
 * “duck” the output signal of another source. It is useful when the volume of a source needs to be tied
 * to the volume of another source, so that the sounds don't overlap.
 * Still, this is implementation works regardless of the presence of the sidechain.
 * Actually, the constructor instantiates a compressor that produces its compression based on the input volume
 * of the source it is attached to, which means there is initially no sidechain.
 */
public class CompressorWithSidechaining extends UGen implements DataBeadReceiver {

    private Compressor compressor;

    public CompressorWithSidechaining(float threshold, float attack, float decay, float ratio, UGen sidechained) {
        super(AudioContextManager.getAudioContext());
        this.compressor = new Compressor(AudioContext.getDefaultContext(), 2, sidechained)
                .setThreshold(threshold).setAttack(attack).setDecay(decay).setRatio(ratio);
    }

    /**
     * Allows to remove the sidechain. After this method is over, the compressor processes the audio based on
     * the volume of the source it is attached to.
     */
    public void removeSidechain() {
        this.compressor.setSideChain(null);
    }

    /**
     * Allows to connect a sidechain. After this method is over, the compressor processes the audio based on
     * the volume of the given source.
     * @param sidechained the {@link UGen} that represents the source the compressor bases its compression on.
     */
    public void connectSidechain(UGen sidechained) {
        this.compressor.setSideChain(sidechained);
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
