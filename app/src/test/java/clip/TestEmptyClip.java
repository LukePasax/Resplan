package clip;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import daw.core.clip.*;

class TestEmptyClip {

	@Test
	void testEmptyClipCreation() {
		RPClip emptyClip = new EmptyClip();
		assertTrue(emptyClip.isEmpty());
		assertEquals(emptyClip.getDuration(), 0);
	}
	
	@Test
	void testEmptyClipDuration() {
		RPClip clip = new EmptyClip();
		clip.setDuration(1000);
		assertEquals(clip.getDuration(), 1000);
	}
	
	@Test
	void testEmptyClipExceptions() {
		RPClip clip = new EmptyClip();
		assertThrows(IllegalStateException.class, ()->clip.setContentPosition(10));
		assertThrows(IllegalStateException.class, ()->clip.getContentPosition());
		assertThrows(IllegalStateException.class, ()->clip.getContent());
	}

}
