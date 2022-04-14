package daw.manager;

import daw.core.clip.RPClip;
import planning.RPPart;

public interface RPClipLinker {
	
	void addClipReferences(RPClip clip, RPPart part);
	
	RPClip getClip(RPPart part);

}
