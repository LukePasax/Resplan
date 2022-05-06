package planning;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * This class tests different types of part
 */
public class TestPart {
	
	/**
	 * Tests a part of type EFFECTS
	 */
	@Test
	public void testEffectsPart() {
		final RPPart part = new EffectsPart("Effects part");
		assertEquals("Effects part", part.getTitle());
		assertEquals(Optional.empty(), part.getDescription());
		part.addDescription("Description of effects part");
		assertEquals(Optional.of("Description of effects part"), part.getDescription());
		assertFalse(part.isTextPresent());
		assertEquals(Optional.empty(), part.getText());
	}
	
	/**
	 * Tests a part of type SOUNDTRACK
	 */
	@Test
	public void testSoundtrackPart() {
		final RPPart part = new EffectsPart("Soundtrack part");
		assertEquals("Soundtrack part", part.getTitle());
		assertEquals(Optional.empty(), part.getDescription());
		part.addDescription("Description of soundtrack part");
		assertEquals(Optional.of("Description of soundtrack part"), part.getDescription());
		assertFalse(part.isTextPresent());
		assertEquals(Optional.empty(), part.getText());
	}
}
