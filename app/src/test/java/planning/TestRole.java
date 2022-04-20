package planning;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * This class tests different types of role
 */
public class TestRole {
	
	/**
	 * Tests a role of type EFFECTS
	 */
	@Test
	public void testEffectsRole() {
		final RPRole role = new EffectsRole("Effects role");
		assertEquals("Effects role", role.getTitle());
		assertEquals(Optional.empty(), role.getDescription());
		role.addDescription("Description of effects role");
		assertEquals(Optional.of("Description of effects role"), role.getDescription());
		assertFalse(role.isSpeakerPresent());
		assertEquals(Optional.empty(), role.getSpeaker());
	}
	
	/**
	 * Tests a role of type SOUNDTRACK
	 */
	@Test
	public void testSoundtrackRole() {
		final RPRole role = new EffectsRole("Soundtrack role");
		assertEquals("Soundtrack role", role.getTitle());
		assertEquals(Optional.empty(), role.getDescription());
		role.addDescription("Description of soundtrack role");
		assertEquals(Optional.of("Description of soundtrack role"), role.getDescription());
		assertFalse(role.isSpeakerPresent());
		assertEquals(Optional.empty(), role.getSpeaker());
	}
}
