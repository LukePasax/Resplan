package planning;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * This class tests a {@link planning.RPTimeline} using some {@link planning.RPSection}
 */
public class TestTimeline {
	
	private final RPTimeline timeline = new TimelineImpl();
	final RPSection s1 = new SectionImpl("Section1", "This is the first section", 2500.0);
	final RPSection s2 = new SectionImpl("Section2", "This is the second section", 1600.0);
	final RPSection s3 = new SectionImpl("Section3", "This is the third section", 1400.0);
	final RPSection s4 = new SectionImpl("Section4", "This is the fourth section", 1300.0);
	
	/**
	 * Tests the addition of some sections to the timeline
	 */
	@Test
	public void testAdd() {
		assertTrue(this.timeline.addSection(0.0, s1));
		assertFalse(this.timeline.addSection(0.0, s2));
		assertFalse(this.timeline.addSection(1500.0, s3));
		assertTrue(this.timeline.addSection(2501.0, s4));
	}
	
	/**
	 * Tests the removal of a section from a timeline
	 */
	@Test
	public void testRemove() {
		assertNotEquals(Optional.of(s4), timeline.getSection(2501));
		timeline.removeSection(s4);
		assertEquals(Optional.empty(), timeline.getSection(2501));
	}
	
	/**
	 * Tests the method of a timeline that allows to get its total duration
	 */
	@Test
	public void testOverallDuration() {
		this.timeline.addSection(2501.0, new SectionImpl("Section5", "This is the fifth section", 1300.0));
		this.timeline.addSection(3802.0, new SectionImpl("Section6", "This is the sixth section", 6515.0));
		this.timeline.addSection(10318.0, new SectionImpl("Section7", "This is the seventh section", 4200.0));
		this.timeline.addSection(14519.0, new SectionImpl("Section8", "This is the eighth section", 3670.0));
		assertEquals(18189.0, timeline.getOverallDuration());
		
		final RPTimeline timeline2 = new TimelineImpl();
		assertEquals(0.0, timeline2.getOverallDuration());
	}
}
