package planning;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import planning.RPPart.PartType;

public class TestSpeechPart {

	final RPPart part = new SpeechPart("Text", "This is a text speech part");
	
	@Test
	public void basicTest() {
		assertEquals("Text", part.getTitle());
		assertEquals("This is a text speech part", part.getDescription());
		assertEquals(PartType.SPEECH, part.getType());
	}
	
	@Test
	public void testTextInSpeechRole() {
		assertFalse(part.isTextPresent());
		
		final TextFactory tf = new TextFactoryImpl();
		final Text testText = tf.createFromString(36, "This is a text for test\n");
		
		part.addText(testText);
		assertTrue(part.isTextPresent());
		try {
			assertEquals("This is a text for test\n", part.getText().getContent());
		} catch (Exception e) {}
		assertEquals(36, part.getText().getSize());
	}
}
