package clip;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import daw.core.clip.*;

class TestFileClip {
	
	static final String SEP = System.getProperty("file.separator");

	@Test
	void testFileClipCreation() {
		File content = new File(System.getProperty("user.dir") + SEP + "src" +
				SEP + "test" + SEP + "resources"+ SEP + "Alergy - Brain in the Jelly.wav");
		RPClip fileClip = new FileClip(content);
		assertFalse(fileClip.isEmpty());
		assertEquals(fileClip.getDuration(), 0);
	}
	
	@Test
	void testFileClipCreationExceptions() {
		File content = new File(System.getProperty("user.dir") + SEP + "src" +
				SEP + "test" + SEP + "resources"+ SEP + "NotARealFile.wav");
		assertThrows(IllegalArgumentException.class, ()->new FileClip(content));
	}


}
