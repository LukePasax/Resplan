package daw.core.clip;

import java.io.IOException;

import daw.core.channel.RPChannel;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;

public interface ClipPlayerFactory {
	
	RPClipPlayer createClipPlayer(RPClip clip, RPChannel channel) throws IOException, OperationUnsupportedException, FileFormatException;
	
	RPClipPlayer createClipPlayerWithActiveCut(RPClip clip, RPChannel channel, double cut) throws IOException, OperationUnsupportedException, FileFormatException;

}
