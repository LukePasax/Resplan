package clip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import daw.core.clip.ClipNotFoundException;
import daw.core.clip.EmptyClip;
import daw.core.clip.RPClip;
import daw.core.clip.RPTapeChannel;
import daw.core.clip.TapeChannel;
import javafx.util.Pair;

class TestTapeChannel {

	@Test
	void testTapeChannelCreation() {
		RPTapeChannel tapeChannel = new TapeChannel();
		assertTrue(tapeChannel.isEmpty());
	}
	
	@Test
	void testAddingOneClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		assertEquals(tapeChannel.getClipAt(0), Optional.empty());
		final double duration = 500;
		RPClip<?> clip = new EmptyClip("title", duration);
		tapeChannel.insertRPClip(clip, 0);
		assertFalse(tapeChannel.isEmpty());
	}
	
	@Test
	void testGetClipAt() {
		RPTapeChannel tapeChannel = new TapeChannel();
		final double duration = 500;
		RPClip<?> clip = new EmptyClip("title", duration);
		tapeChannel.insertRPClip(clip, 0);
		assertEquals(tapeChannel.getClipAt(0), Optional.of(new Pair<>(0.0, clip)));
		assertEquals(tapeChannel.getClipAt(duration / 2), Optional.of(new Pair<>(0.0, clip)));
		assertEquals(tapeChannel.getClipAt(duration - 0.1), Optional.of(new Pair<>(0.0, clip)));
		assertEquals(tapeChannel.getClipAt(duration), Optional.empty());
	}
	
	@Test
	void testRemoveOneClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		assertEquals(tapeChannel.getClipAt(0), Optional.empty());
		double duration = 500;
		RPClip<?> clip = new EmptyClip("title", duration);
		tapeChannel.insertRPClip(clip, 0);
		assertEquals(tapeChannel.getClipAt(0), Optional.of(new Pair<>(0.0, clip)));
		assertThrows(ClipNotFoundException.class, ()->tapeChannel.removeClip(200));
		try {
			tapeChannel.removeClip(0);
		} catch (ClipNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(tapeChannel.getClipAt(0), Optional.empty());
	}
	
	@Test
	void testGetClipWithTimeIteratorEmptyTimeline() {
		RPTapeChannel tapeChannel = new TapeChannel();
		assertFalse(tapeChannel.getClipWithTimeIterator().hasNext());
	}
	
	@Test
	void testGetClipWithTimeIterator() {
		RPTapeChannel tapeChannel = new TapeChannel();
		for (int i = 0; i < 10; i++) {
			RPClip<?> clip = new EmptyClip("title" + i, 10);
			tapeChannel.insertRPClip(clip, i * 10);
		}
		Iterator<Pair<Double, RPClip<?>>> iterator = tapeChannel.getClipWithTimeIterator();
		for (double i = 0; i < 100; i = i + 10) {
			assertTrue(iterator.hasNext());
			var x = iterator.next();
			assertEquals(x.getKey(), i);
		}
		assertFalse(iterator.hasNext());
	}
	
	@Test
	void testAddingAndOverwriteClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		for (int i = 0; i < 10; i++) {
			RPClip<?> clip = new EmptyClip("title" + i, 100);
			tapeChannel.insertRPClip(clip, i * 10);
		}
		Iterator<Pair<Double, RPClip<?>>> iterator = tapeChannel.getClipWithTimeIterator();
		for (double i = 0; i < 90; i = i + 10) {
			assertTrue(iterator.hasNext());
			var x = iterator.next();
			assertEquals(x.getKey(), i);
			assertEquals(x.getValue().getDuration(), 10.0);
		}
		assertTrue(iterator.hasNext());
		var x = iterator.next();
		assertEquals(x.getKey(), 90);
		assertEquals(x.getValue().getDuration(), 100.0);
		assertFalse(iterator.hasNext());
	}
	
	@Test
	void testAddingClipException() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip<?> clip = new EmptyClip("title", 100);
		tapeChannel.insertRPClip(clip, 10);
		RPClip<?> clip2 = new EmptyClip("title2", 50);
		assertThrows(IllegalStateException.class, () -> tapeChannel.insertRPClip(clip2, 10));
	}
	
	@Test
	void testMoveOneClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip<?> clip = new EmptyClip("title", 10);
		tapeChannel.insertRPClip(clip, 0);
		assertEquals(tapeChannel.getClipAt(10), Optional.empty());
		try {
			tapeChannel.move(0, 10);
		} catch (ClipNotFoundException e) {	
			e.printStackTrace();
			fail();
		}
		assertEquals(tapeChannel.getClipAt(10), Optional.of(new Pair<>(10.0, clip)));
		assertThrows(IllegalArgumentException.class, () -> tapeChannel.move(10, -2));
		assertThrows(ClipNotFoundException.class, () -> tapeChannel.move(0, 10));
	}
	
	@Test
	void testMoveAndOverrideClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip<?> clip = new EmptyClip("title", 100);
		RPClip<?> clip2 = new EmptyClip("title2", 100);
		RPClip<?> clip3 = new EmptyClip("title3", 100);
		tapeChannel.insertRPClip(clip, 0);
		tapeChannel.insertRPClip(clip2, 200);
		tapeChannel.insertRPClip(clip3, 400);
		try {
			tapeChannel.move(200, 50);
		} catch (ClipNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(tapeChannel.getClipAt(50).get().getValue().getDuration(), 100.0);
		assertEquals(tapeChannel.getClipAt(0).get().getValue().getDuration(), 50.0);
		try {
			tapeChannel.move(0, 450);
		} catch (ClipNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		assertEquals(tapeChannel.getClipAt(0), Optional.empty());
		assertEquals(tapeChannel.getClipAt(50).get().getValue().getDuration(), 100.0);
		assertEquals(tapeChannel.getClipAt(400).get().getValue().getDuration(), 50.0);
		assertEquals(tapeChannel.getClipAt(450).get().getValue().getDuration(), 50.0);
		assertEquals(tapeChannel.getClipAt(500), Optional.empty());
	}

}
