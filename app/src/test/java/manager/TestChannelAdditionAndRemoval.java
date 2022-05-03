package manager;

import com.sun.jdi.event.ExceptionEvent;
import daw.manager.Manager;
import daw.manager.RPManager;
import org.junit.jupiter.api.Test;
import planning.RPRole;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestChannelAdditionAndRemoval {

    private final RPManager manager = new Manager();

    @Test
    public void testAddition() {
        manager.addChannel(RPRole.RoleType.SPEECH, "Paolo", Optional.empty());
        manager.addChannel(RPRole.RoleType.SPEECH, "Stefano", Optional.of("Mio cugino"));
        manager.addChannel(RPRole.RoleType.EFFECTS, "Cheer effect", Optional.empty());
        manager.addChannel(RPRole.RoleType.SOUNDTRACK, "Background music", Optional.empty());
        assertEquals(2, manager.getGroupList("Speech").size());
        assertEquals(1, manager.getGroupList("Effects").size());
        assertEquals(1, manager.getGroupList("Soundtrack").size());
    }

    @Test
    public void testRemoval() {
        manager.addChannel(RPRole.RoleType.SPEECH, "Paolo", Optional.empty());
        manager.addChannel(RPRole.RoleType.SPEECH, "Stefano", Optional.of("Mio cugino"));
        manager.removeChannel("Paolo");
        assertEquals(1, manager.getGroupList("Speech").size());
        assertThrows(NoSuchElementException.class, () -> manager.removeChannel("Giacomo"));
    }
}
