package storing;

import controller.general.ControllerImpl;
import daw.core.clip.ClipNotFoundException;
import daw.core.mixer.Mixer;
import daw.manager.ImportException;
import daw.utilities.AudioContextManager;
import org.junit.jupiter.api.Test;
import planning.RPPart;
import planning.RPRole;

import java.io.File;
import java.util.Optional;

public class TestApplication {

    private static final String SEP = System.getProperty("file.separator");
    private static final String DIR = System.getProperty("user.dir");
    private static final File FILE = new File(DIR + SEP + "src" + SEP + "test" + SEP +
             "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav");

    @Test
    public void all() {
        final ControllerImpl controller = new ControllerImpl();
        System.out.println(AudioContextManager.getAudioContext().out.getConnectedInputs());
        controller.getManager().addChannel(RPRole.RoleType.EFFECTS, "ciao", Optional.empty());
        //System.out.println(((Mixer) controller.getManager().getMixer()).getConnectedInputsToMaster());
        try {
            controller.getManager().addClip(RPPart.PartType.EFFECTS, "part1", Optional.empty(), "ciao",
                    0.0, 0.50, Optional.of(FILE));
            controller.start();
        } catch (ImportException e) {
            e.printStackTrace();
        }
    }

}
