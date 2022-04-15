package planning;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestText {
	
	/*
	 * Aggiungere test per verificare contenuto di file valido
	 */
	
	@Test
	public void testCreationFromFile() {
		final TextFactory tf = new TextFactoryImpl();
		
		try {
			tf.createFromFile(48, "C/Users/Test/test.txt").getContent();
			fail();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
}
