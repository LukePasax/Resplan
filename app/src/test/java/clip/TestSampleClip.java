package clip;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.Test;
import daw.core.clip.*;
import net.beadsproject.beads.data.Sample;

class TestSampleClip {

	static final String SEP = System.getProperty("file.separator");

	@Test
	void testSampleClipCreation() {
		File content = new File(System.getProperty("user.dir") + SEP + "src" +
				SEP + "test" + SEP + "resources"+ SEP + "Alergy - Brain in the Jelly.wav");
		RPClip sampleClip;
		try {
			sampleClip = new SampleClip(content);
			assertFalse(sampleClip.isEmpty());
			assertEquals(sampleClip.getDuration(), new Sample(content.getAbsolutePath()).getLength());
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void testFileClipCreationExceptions() {
		File content = new File(System.getProperty("user.dir") + SEP + "src" +
				SEP + "test" + SEP + "resources"+ SEP + "NotAnAudioFile.txt");
		assertThrows(Exception.class, ()->new SampleClip(content));
	}
}
