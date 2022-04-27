package engine;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import daw.core.channel.BasicChannelFactory;
import daw.core.channel.RPChannel;
import daw.core.clip.*;
import daw.engine.*;
import daw.general.HashMapToSet;
import daw.general.MapToSet;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

class TestClipPlayerNotifier {
	
	private Clock clock = new Clock();
	
	private RPChannel channel = new BasicChannelFactory().basic();
	
	private MapToSet<Long, RPClipPlayer> observers = new HashMapToSet<>();
	
	static final String SEP = System.getProperty("file.separator");
	
	private void initializeRandomObservers() throws IOException, OperationUnsupportedException, FileFormatException {
		for(int i = 0; i<100*Math.random(); i++) {
			SampleClip clip = new SampleClip(new File(System.getProperty("user.dir") + SEP + "src" +
					SEP + "test" + SEP + "resources"+ SEP + "audio" + SEP + "Alergy - Brain in the Jelly.wav"));
			this.observers.put(clock.timeToClockSteps(Math.random()*clock.getClockMaxTime()), new SampleClipPlayerFactory().createSampleClipPlayer(clip, channel));
		}	
	}
	
	@Test
	void randomObserversPlay() throws IOException, OperationUnsupportedException, FileFormatException {
		this.initializeRandomObservers();
		ClipPlayerNotifier cpn = new ClipPlayerNotifier(clock, observers);
		observers.entrySet().stream().forEach(x->{
			clock.setTime(clock.clockStepToTime(x.getKey()));
			cpn.update();
			x.getValue().forEach(player->{
				assertFalse(player.isPlaused());
			});
		});
	}
	
	@Test
	void randomObserversPause() throws IOException, OperationUnsupportedException, FileFormatException {
		this.initializeRandomObservers();
		ClipPlayerNotifier cpn = new ClipPlayerNotifier(clock, observers);
		observers.entrySet().stream().forEach(x->{
			x.getValue().forEach(player->{
				player.play();
				assertFalse(player.isPlaused());
			});
		});
		cpn.notifyStopped();
		observers.entrySet().stream().forEach(x->{
			x.getValue().forEach(player->{
				assertTrue(player.isPlaused());
			});
		});
		
	}

}
