package planning;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import planning.RPRole.RoleType;

/**
 * This class tests a {@link planning.SpeechRole}
 */
public class TestSpeechRole {
	
	private final RPRole role = new SpeechRole("Role1", "This is the first role");
	
	/**
	 * Tests basic elements of a speech role
	 */
	@Test
	public void basicTest() {
		assertEquals("Role1", this.role.getTitle());
		assertEquals("This is the first role", this.role.getDescription());
		assertEquals(RoleType.SPEECH, this.role.getType());
	}
	
	/**
	 * Tests speaker into a speech role
	 */
	@Test
	public void testSpeaker() {
		final Speaker s = new SimpleSpeaker(1, "Mario", "Bianchi");
		assertFalse(this.role.isSpeakerPresent());
		this.role.addSpeaker(s);
		assertTrue(this.role.isSpeakerPresent());
		assertEquals(Optional.of(s), this.role.getSpeaker());
		
		final RPRole role2 = new SpeechRole("Role2", "This is the second role");
		assertEquals(Optional.empty(), role2.getSpeaker());
		
		final RPRole role3 = new SoundtrackRole("Role3", "This is the third role");
		assertFalse(role3.isSpeakerPresent());
		assertEquals(Optional.empty(), role3.getSpeaker());
	}
}
