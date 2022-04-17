package daw.manager;

import daw.core.clip.RPClip;
import planning.RPPart;

/**
 * This interface represents a class to link a {@link RPPart} to its corresponding {@link RPClip}
 */

public interface RPClipLinker {

	/**
	 * This method links the given {@link RPPart} to the given {@link RPClip}
	 * @param clip the {@link RPClip} to link
	 * @param part the {@link RPPart} to link
	 */
	void addClipReferences(RPClip clip, RPPart part);

	/**
	 * A method that returns the {@link RPClip} linked to the given {@link RPPart}
	 * @param part the {@link RPPart} linked
	 * @return the {@link RPClip} linked
	 */
	RPClip getClip(RPPart part);

}
