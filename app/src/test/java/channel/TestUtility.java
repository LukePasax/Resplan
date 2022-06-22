package channel;

import daw.core.audioprocessing.RPEffect;
import net.beadsproject.beads.core.UGen;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtility {

    public List<Class> getList(List<? extends UGen> ugens) {
        return ugens.stream().map(Object::getClass).collect(Collectors.toUnmodifiableList());
    }


    public boolean effectsAreConnected(RPEffect from, RPEffect to) {
        return to.getGainIn().getConnectedInputs().contains(from.getGainOut());
    }

}
