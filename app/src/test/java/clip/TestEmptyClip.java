package clip;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import daw.core.clip.*;

class TestEmptyClip {

	@Test
	void testEmptyClipCreation() {
		RPClip<?> emptyClip = new EmptyClip("ciao");
		assertTrue(emptyClip.isEmpty());
		assertEquals(emptyClip.getDuration(), RPClip.DEFAULT_DURATION);
	}
	
	@Test
	void testEmptyClipDuration() {
		RPClip<?> clip = new EmptyClip("ciao");
		clip.setDuration(1000);
		assertEquals(clip.getDuration(), 1000);
	}
	
	@Test
	void testEmptyClipExceptions() {
		RPClip<?> clip = new EmptyClip("ciao");
		assertThrows(UnsupportedOperationException.class, ()->clip.setContentPosition(10));
		assertThrows(UnsupportedOperationException.class, ()->clip.getContentPosition());
		assertThrows(UnsupportedOperationException.class, ()->clip.getContent());
	}

}
