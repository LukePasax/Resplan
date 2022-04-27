package planning;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class tests a {@link planning.Text}
 */
public class TestText {
	
	final static String SEPARATOR = System.getProperty("file.separator");
	
	/**
	 * Tests the creation of a text starting from a wrong file
	 */
	@Test
	public void testCreationFromWrongFile() {
		final TextFactory tf = new TextFactoryImpl();
		
		try {
			tf.createFromFile("C/Users/Test/test.txt").getContent();
			fail();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	/**
	 * Tests the creation of a text starting from a right file
	 */
	@Test
	public void testCreationFromRightFile() {
		
		final TextFactory tf = new TextFactoryImpl();
		String content = "";
		try {
			content = tf.createFromFile(System.getProperty("user.dir") 
					+ SEPARATOR + "src" + SEPARATOR + "test" + SEPARATOR + "resources" 
					+ SEPARATOR + "text" + SEPARATOR + "TextTest.txt").getContent();
		} catch (Exception e) {}

		assertDoesNotThrow(() -> tf.createFromFile(System.getProperty("user.dir") 
													+ SEPARATOR + "src" + SEPARATOR + "test" + SEPARATOR + "resources" 
													+ SEPARATOR + "text" + SEPARATOR + "TextTest.txt").getContent());
		
		assertEquals(content, "File di testo di\r\n" 
								+ "esempio, per prova di\r\n" 
								+ "apertura di testo\r\n" 
								+ "da file");
    }
}
