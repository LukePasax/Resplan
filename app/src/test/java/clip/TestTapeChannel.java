package clip;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import daw.core.clip.*;
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
		double duration = 500;
		RPClip clip = new EmptyClip(duration);
		tapeChannel.insertRPClip(clip, 0);
		assertFalse(tapeChannel.isEmpty());
	}
	
	@Test
	void testGetClipAt() {
		RPTapeChannel tapeChannel = new TapeChannel();
		double duration = 500;
		RPClip clip = new EmptyClip(duration);
		tapeChannel.insertRPClip(clip, 0);
		assertEquals(tapeChannel.getClipAt(0), Optional.of(new Pair<>(0.0, clip)));
		assertEquals(tapeChannel.getClipAt(duration/2), Optional.of(new Pair<>(0.0, clip)));
		assertEquals(tapeChannel.getClipAt(duration-0.1), Optional.of(new Pair<>(0.0, clip)));
		assertEquals(tapeChannel.getClipAt(duration), Optional.empty());
		
	}
	
	@Test
	void testRemoveOneClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		assertEquals(tapeChannel.getClipAt(0), Optional.empty());
		double duration = 500;
		RPClip clip = new EmptyClip(duration);
		tapeChannel.insertRPClip(clip, 0);
		assertEquals(tapeChannel.getClipAt(0), Optional.of(new Pair<>(0.0, clip)));
		assertThrows(IllegalArgumentException.class, ()->tapeChannel.removeRPClip(200));
		tapeChannel.removeRPClip(0);
		assertEquals(tapeChannel.getClipAt(0), Optional.empty());
	}
	
	@Test
	void testGetClipWithTimeIteratorEmptyTimeline() {
		RPTapeChannel tapeChannel = new TapeChannel();
		assertEquals(tapeChannel.getClipWithTimeIterator(), Optional.empty());
	}
	
	@Test
	void testGetClipWithTimeIterator() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip clip = new EmptyClip(10);
		for(int i = 0; i<10; i++) {
			tapeChannel.insertRPClip(clip, i*10);
		}
		Iterator<Pair<Double, RPClip>> iterator = tapeChannel.getClipWithTimeIterator().get();
		for(double i = 0; i<100; i=i+10) {
			assertTrue(iterator.hasNext());
			var x = iterator.next();
			assertEquals(x.getKey(), i);
		}
		assertFalse(iterator.hasNext());
	}
	
	@Test
	void testAddingAndOverwriteClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip clip = new EmptyClip(100);
		for(int i = 0; i<10; i++) {
			tapeChannel.insertRPClip(clip, i*10);
		}
		Iterator<Pair<Double, RPClip>> iterator = tapeChannel.getClipWithTimeIterator().get();
		for(double i = 0; i<100; i=i+10) {
			assertTrue(iterator.hasNext());
			var x = iterator.next();
			assertEquals(x.getKey(), i);
			assertEquals(x.getValue().getDuration(), 10.0);
		}
		assertFalse(iterator.hasNext());
	}
	
	@Test
	void testMoveOneClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip clip = new EmptyClip(10);
		tapeChannel.insertRPClip(clip, 0);
		assertEquals(tapeChannel.getClipAt(10), Optional.empty());
		tapeChannel.move(0, 10);
		assertEquals(tapeChannel.getClipAt(10), Optional.of(new Pair<>(10.0, clip)));
		assertThrows(IllegalArgumentException.class, ()->tapeChannel.move(10, -2));
		assertThrows(IllegalArgumentException.class, ()->tapeChannel.move(0, 10));
	}
	
	@Test
	void testMoveAndOverrideClip() {
		RPTapeChannel tapeChannel = new TapeChannel();
		RPClip clip = new EmptyClip(100);
		tapeChannel.insertRPClip(clip, 0);
	}

}
