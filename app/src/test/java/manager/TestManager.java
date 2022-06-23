package manager;

import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import daw.manager.Manager;
import daw.manager.RPManager;
import org.junit.jupiter.api.Test;
import planning.RPPart;
import planning.RPRole;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestManager {

    private final static String SEP = System.getProperty("file.separator");
    private final RPManager manager = new Manager();

    @Test
    public void testChannelAddition() {
        manager.addChannel(RPRole.RoleType.SPEECH, "Paolo", Optional.empty());
        manager.addChannel(RPRole.RoleType.SPEECH, "Stefano", Optional.of("Mio cugino"));
        manager.addChannel(RPRole.RoleType.EFFECTS, "Cheer effect", Optional.empty());
        manager.addChannel(RPRole.RoleType.SOUNDTRACK, "Background music", Optional.empty());
        assertEquals(2, manager.getGroupList("Speech").size());
        assertEquals(1, manager.getGroupList("Effects").size());
        assertEquals(1, manager.getGroupList("Soundtrack").size());
    }

    @Test
    public void testChannelRemoval() {
        manager.addChannel(RPRole.RoleType.SPEECH, "Paolo", Optional.empty());
        manager.addChannel(RPRole.RoleType.SPEECH, "Stefano", Optional.of("Mio cugino"));
        manager.removeChannel("Paolo");
        assertEquals(1, manager.getGroupList("Speech").size());
        assertThrows(NoSuchElementException.class, () -> manager.removeChannel("Giacomo"));
    }

    @Test
    public void testClipAddition() {
        manager.addChannel(RPRole.RoleType.SPEECH, "Paolo", Optional.empty());
        manager.addChannel(RPRole.RoleType.SPEECH, "Stefano", Optional.of("Mio cugino"));
        try {
            manager.addClip(RPPart.PartType.SPEECH, "Song", Optional.empty(), "Paolo",
                    0.0d, 240000d, Optional.empty());
            manager.addClip(RPPart.PartType.SPEECH, "Sang", Optional.empty(), "Stefano",
                    0.0d, 240000d, Optional.of(new File(System.getProperty("user.dir") + SEP + "src"
                            + SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav")));
            assertThrows(IllegalStateException.class, () -> manager.addClip(RPPart.PartType.SPEECH, "Sung", Optional.empty(), "Stefano",
                    0.0d, 240000d, Optional.of(new File(System.getProperty("user.dir") + SEP + "src"
                            + SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav"))));
            assertThrows(IllegalArgumentException.class, () -> manager.addClip(RPPart.PartType.SPEECH, "Sang", Optional.empty(), "Stefano",
                    240000d, 240000d, Optional.of(new File(System.getProperty("user.dir") + SEP + "src"
                            + SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav"))));

        } catch (ImportException ignored) {
        }
        assertEquals(1, manager.getPartList("Paolo").size());
        assertEquals(1, manager.getPartList("Stefano").size());
    }

    @Test
    public void testClipRemoval() {
        manager.addChannel(RPRole.RoleType.SPEECH, "Paolo", Optional.empty());
        manager.addChannel(RPRole.RoleType.SPEECH, "Stefano", Optional.of("Mio cugino"));
        try {
            manager.addClip(RPPart.PartType.SPEECH, "Song", Optional.empty(), "Paolo",
                    0.0d, 240000d, Optional.empty());
            manager.addClip(RPPart.PartType.SPEECH, "Sang", Optional.empty(), "Stefano",
                    0.0d, 240000d, Optional.of(new File(System.getProperty("user.dir") + SEP + "src"
                            + SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav")));
            manager.addClip(RPPart.PartType.SPEECH, "Sung", Optional.empty(), "Stefano",
                    240000d, 240000d, Optional.of(new File(System.getProperty("user.dir") + SEP + "src"
                            + SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav")));
            manager.addClip(RPPart.PartType.SPEECH, "Sing", Optional.empty(), "Paolo",
                    240000d, 240000d, Optional.empty());
            manager.removeClip("Paolo", "Song", manager.getClipTime("Song", "Paolo"));
            manager.removeClip("Stefano", "Sang", manager.getClipTime("Sang", "Stefano"));
            assertThrows(NoSuchElementException.class, () -> manager.removeClip("Paolo", "Song",
                    manager.getClipTime("Song", "Paolo")));

        } catch (ImportException ignored) {
        } catch (ClipNotFoundException e) {
            fail();
        }
        assertEquals(1, manager.getPartList("Paolo").size());
        assertEquals(1, manager.getPartList("Stefano").size());
    }
}
