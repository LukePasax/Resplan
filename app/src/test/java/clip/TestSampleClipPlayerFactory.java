package clip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import daw.core.channel.BasicChannelFactory;
import daw.core.channel.RPChannel;
import daw.core.clip.ClipPlayerFactory;
import daw.core.clip.EmptyClip;
import daw.core.clip.RPClip;
import daw.core.clip.RPClipPlayer;
import daw.core.clip.SampleClip;
import daw.core.clip.SampleClipPlayerFactory;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

class TestSampleClipPlayerFactory {
	
	static final String SEP = System.getProperty("file.separator");

	@Test
	void testSamplePlayerCreation() {
		ClipPlayerFactory factory = new SampleClipPlayerFactory();
		RPChannel channel = new BasicChannelFactory().basic();
		try {
			RPClip<?> clip = new SampleClip("title", new File(System.getProperty("user.dir") + SEP + "src"
				+ SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav"));
			RPClipPlayer player = factory.createClipPlayer(clip, channel);
			assertEquals(clip.getContentPosition(), player.getPlaybackPosition());
			assertTrue(player.isPaused());
		} catch (IOException | OperationUnsupportedException | FileFormatException e) {
			e.printStackTrace();
			fail("Exception throwed");
		}	
	}
	
	@Test
	void testSamplePlayerCreationWithCut() {
		final double cutTime = 0.543;
		ClipPlayerFactory factory = new SampleClipPlayerFactory();
		RPChannel channel = new BasicChannelFactory().basic();
		try {
			RPClip<?> clip = new SampleClip("title", new File(System.getProperty("user.dir") + SEP + "src"
				+ SEP + "test" + SEP + "resources" + SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav"));
			RPClipPlayer player = factory.createClipPlayerWithActiveCut(clip, channel, cutTime);
			assertEquals(clip.getContentPosition(), player.getPlaybackPosition());
			assertTrue(player.isCutActive());
			assertEquals(cutTime, player.getCutTime());
		} catch (IOException | OperationUnsupportedException | FileFormatException e) {
			e.printStackTrace();
			fail("Exception throwed");
		}	
	}
	
	@Test
	void testSamplePlayerFactoryException() {
		ClipPlayerFactory factory = new SampleClipPlayerFactory();
		RPChannel channel = new BasicChannelFactory().basic();
		RPClip<?> clip = new EmptyClip("ciao");
		assertThrows(IllegalArgumentException.class, () -> factory.createClipPlayer(clip, channel));
	}

}
