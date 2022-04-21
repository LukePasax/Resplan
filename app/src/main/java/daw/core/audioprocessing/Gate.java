package daw.core.audioprocessing;

import Resplan.AudioContextManager;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.ugens.Compressor;

public class Gate extends RPEffect {

    private Compressor compressor;

    public Gate(int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.compressor = new Compressor(AudioContextManager.getAudioContext(), channels);
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
