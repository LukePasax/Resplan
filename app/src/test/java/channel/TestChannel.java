package channel;

import daw.core.audioprocessing.Gate;
import daw.core.audioprocessing.HighPassFilter;
import daw.core.channel.BasicChannelFactory;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestChannel {

    private final TestUtility testUtility = new TestUtility();

    @Test
    public void testAll() {
        final var channel = new BasicChannelFactory().gated();
        assertTrue(channel.isEnabled());
        assertTrue(channel.isProcessingUnitPresent());
        assertEquals(List.of(HighPassFilter.class, Gate.class),
                this.testUtility.getList(channel.getProcessingUnit().get().getEffects()));
        assertEquals(100, channel.getVolume());
        channel.setVolume(70);
        assertEquals(70, channel.getVolume());
        assertEquals(0.70, channel.getOutput().getGain(), 0.01);
        assertEquals(2, channel.getPanner().getOuts());
        channel.disable();
        assertFalse(channel.isEnabled());
        try {
            channel.setVolume(110);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

}
