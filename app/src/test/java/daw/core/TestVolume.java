package daw.core;

import daw.general.BasicVolume;
import daw.general.Volume;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestVolume {

    private Volume volume = new BasicVolume(15);

    @Test
    public void initial() {
        assertEquals(15, this.volume.getVolume());
    }

    @Test
    public void testSetVolume() {
        this.volume.setVolume(50);
        assertEquals(50, this.volume.getVolume());
        this.volume.setVolume(200);
        assertEquals(50, this.volume.getVolume());
    }

    @Test
    public void testIncrement() {
        this.volume.increment();
        assertEquals(16, this.volume.getVolume());
        while (this.volume.getVolume() <= 100) {
            this.volume.increment();
        }
        assertEquals(100, this.volume.getVolume());
    }

    @Test
    public void testDecrement() {
        this.volume.setVolume(110);
        this.volume.decrement();
        assertEquals(99, this.volume.getVolume());
        while (this.volume.getVolume() >= 0) {
            this.volume.decrement();
        }
        assertEquals(0, this.volume.getVolume());
    }
}
