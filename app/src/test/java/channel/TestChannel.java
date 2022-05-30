package channel;

import daw.core.audioprocessing.Gate;
import daw.core.audioprocessing.HighPassFilter;
import daw.core.channel.BasicChannelFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestChannel {

    private final TestReflection ref = new TestReflection();

    @Test
    public void testAll() {
        final var channel = new BasicChannelFactory().gated();
        assertTrue(channel.isEnabled());
        assertTrue(channel.isProcessingUnitPresent());
        assertEquals(List.of(HighPassFilter.class, Gate.class),
                this.ref.getList(channel.getProcessingUnit().get().getEffects()));
        assertEquals(100, channel.getVolume());
        channel.setVolume(70);
        assertEquals(70, channel.getVolume());
        assertEquals(0.70, channel.getOutput().getGain(), 0.01);
        try {
            channel.setVolume(110);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
}
