package clip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.File;
import org.junit.jupiter.api.Test;
import daw.core.clip.FileClip;
import daw.core.clip.RPClip;

class TestFileClip {
	
	static final String SEP = System.getProperty("file.separator");

	@Test
	void testFileClipCreation() {
		File content = new File(System.getProperty("user.dir") + SEP + "src"
				+ SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav");
		RPClip<?> fileClip = new FileClip("title", content);
		assertFalse(fileClip.isEmpty());
		assertEquals(fileClip.getDuration(), RPClip.DEFAULT_DURATION);
	}
	
	@Test
	void testFileClipCreationExceptions() {
		File content = new File(System.getProperty("user.dir") + SEP + "src"
				+ SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "NotARealFile.wav");
		assertThrows(IllegalArgumentException.class, () -> new FileClip("title", content));
	}
}
