package planning;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import planning.RPRole.RoleType;

public class TestSpeechRole {
	
	private final RPRole role = new SpeechRole("Role1", "This is the first role");
	
	@Test
	public void basicTest() {
		assertEquals("Role1", this.role.getTitle());
		assertEquals("This is the first role", this.role.getDescription());
		assertEquals(RoleType.SPEECH, this.role.getType());
	}
	
	@Test
	public void testSpeaker() {
		final Speaker s = new SimpleSpeaker(1, "Mario", "Bianchi");
		this.role.addSpeaker(s);
		assertEquals(Optional.of(s), this.role.getSpeaker());
		
		final RPRole role2 = new SpeechRole("Role2", "This is the second role");
		assertEquals(Optional.empty(), role2.getSpeaker());
		
		final RPRole role3 = new SoundtrackRole("Role3", "This is the third role");
		assertEquals(Optional.empty(), role3.getSpeaker());
	}
}
