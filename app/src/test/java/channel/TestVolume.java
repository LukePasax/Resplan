package channel;

import daw.utilities.BasicVolume;
import daw.utilities.Volume;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestVolume {

    @Test
    public void testSetVolume() {
        final Volume vol = new BasicVolume();
        vol.setVolume(50);
        assertEquals(50, vol.getVolume());
        vol.setVolume(200);
        assertEquals(50, vol.getVolume());
    }

    @Test
    public void testIncrement() {
        final Volume vol = new BasicVolume(15, 50);
        vol.increment();
        assertEquals(16, vol.getVolume());
        while (vol.getVolume() < 50) {
            vol.increment();
        }
        assertEquals(50, vol.getVolume());
        vol.increment();
        assertEquals(50, vol.getVolume());
    }

    @Test
    public void testDecrement() {
        final Volume vol = new BasicVolume();
        final int current = vol.getVolume();
        vol.decrement();
        assertEquals(current-1, vol.getVolume());
        while (vol.getVolume() > 0) {
            vol.decrement();
        }
        assertEquals(0, vol.getVolume());
        vol.decrement();
        assertEquals(0, vol.getVolume());
    }

}
