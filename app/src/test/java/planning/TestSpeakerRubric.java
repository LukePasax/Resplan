package planning;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * This class tests a {@link planning.SpeakerRubric} using some {@link planning.Speaker}
 */
public class TestSpeakerRubric {
	
	private final Speaker s1 = new SimpleSpeaker(1, "Gabriele", "Menghi");
	
	private final Speaker s2 = new SimpleSpeaker.Builder(2)
												.firstName("Giacomo")
												.lastName("Sirri")
												.build();
	
	private final Speaker s3 = new SimpleSpeaker(3, "Alessandro", "Antonini");
	
	private final Speaker s4 = new SimpleSpeaker.Builder(4)
												.firstName("Luca")
												.lastName("Pasini")
												.build();
	
	private final Speaker s5 = new SimpleSpeaker(1, "Marco", "Verdi");
	
	/**
	 * Tests the addition of some speakers in the rubric
	 */
	@Test
	public void testAdd() {
		final SpeakerRubric sp = new SimpleSpeakerRubric();
		assertTrue(sp.addSpeaker(s1));
		assertTrue(sp.addSpeaker(s2));
		assertTrue(sp.addSpeaker(s3));
		assertTrue(sp.addSpeaker(s4));
		
		assertFalse(sp.addSpeaker(s5));
		assertFalse(sp.addSpeaker(s1));
	}
	
	/**
	 * Tests the removal of some speakers from the rubric
	 */
	@Test
	public void testRemove() {
		final SpeakerRubric sp = new SimpleSpeakerRubric();
		assertTrue(sp.addSpeaker(s1));
		assertFalse(sp.addSpeaker(s1));
		assertTrue(sp.removeSpeaker(s1));
		assertTrue(sp.addSpeaker(s1));
		
		assertFalse(sp.removeSpeaker(s5));
	}
	
	/**
	 * Tests the search of a speaker into the rubric
	 */
	@Test
	public void testSearch() {
		final SpeakerRubric sp = new SimpleSpeakerRubric();
		sp.addSpeaker(s1);
		sp.addSpeaker(s2);
		sp.addSpeaker(s3);
		sp.addSpeaker(s4);
		
		assertEquals(Optional.of(s2), sp.searchSpeaker(2));
	}
	
	/**
	 * Tests the method of the rubric that allows to get all the speakers present
	 */
	@Test
	public void testBasicGet() {
		final SpeakerRubric sp = new SimpleSpeakerRubric();
		sp.addSpeaker(s1);
		sp.addSpeaker(s2);
		sp.addSpeaker(s3);
		sp.addSpeaker(s4);
		sp.addSpeaker(s5);	//Fails
		
		assertEquals(List.of(s1, s2, s3, s4), sp.getSpeakers());
	}
	
	/**
	 * Tests the method of the rubric that allows to get all the speakers present and matching a filter
	 */
	@Test
	public void testFilteredGet() {
		final SpeakerRubric sp = new SimpleSpeakerRubric();
		sp.addSpeaker(s1);
		sp.addSpeaker(s2);
		sp.addSpeaker(s3);
		sp.addSpeaker(s4);
		sp.addSpeaker(s5);	//Fails
		
		assertEquals(List.of(s1, s2), sp.getFilteredSpeakers(x -> x.getFirstName().startsWith("G")));
	}
}
