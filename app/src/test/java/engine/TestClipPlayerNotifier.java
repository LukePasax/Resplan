package engine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.RPChannel;
import daw.core.clip.*;
import daw.engine.*;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

class TestClipPlayerNotifier {
	
	{
		this.clock = new Clock();
		
		this.channel = new BasicChannelFactory().basic();
		
		this.playersMap = new PlayersMap();
		
		try {
			this.createRandomPlayersMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Clock clock;
	
	private RPChannel channel;
	
	private PlayersMap playersMap;
	
	static final String SEP = System.getProperty("file.separator");
	
	
	private void createRandomPlayersMap() throws IOException, OperationUnsupportedException, FileFormatException {
		for(int i = 0; i<10; i++) {
			SampleClip clip = new SampleClip(new File(System.getProperty("user.dir") + SEP + "src" +
					SEP + "test" + SEP + "resources"+ SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav"));
			this.playersMap.putClipPlayer(Clock.Utility.timeToClockSteps(i/20*Clock.Utility.getClockMaxTime()), new SampleClipPlayerFactory().createClipPlayer(clip, channel));
		}
	}
	
	@Test
	void randomObserversPlay() {
		ClipPlayerNotifier cpn = new ClipPlayerNotifier(clock, playersMap);
		playersMap.entrySet().stream().forEach(x->{
			clock.setTime(Clock.Utility.clockStepToTime(x.getKey()));
			cpn.update();
			x.getValue().forEach(player->{
				assertFalse(player.isPaused(), "clipTime: " + x.getKey());
			});
		});
	}
	
	@Test
	void randomObserversPause() {
		ClipPlayerNotifier cpn = new ClipPlayerNotifier(clock, playersMap);
		playersMap.entrySet().stream().forEach(x->{
			x.getValue().forEach(player->{
				player.play();
				assertFalse(player.isPaused(), "paused: " + player.isPaused());
			});
		});
		cpn.notifyStopped();
		playersMap.entrySet().stream().forEach(x->{
			x.getValue().forEach(player->{
				assertTrue(player.isPaused(), "paused: " + player.isPaused());
			});
		});	
	}

}
