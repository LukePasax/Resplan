package daw.core.effect;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;

public class Sidechain extends UGen implements DataBeadReceiver {

    public Sidechain(AudioContext context) {
        super(context);
    }

    @Override
    public void calculateBuffer() {

    }

    @Override
    public DataBeadReceiver sendData(DataBead db) {
        return null;
    }

}
