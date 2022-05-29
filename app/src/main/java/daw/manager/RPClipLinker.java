package daw.manager;

import daw.core.clip.RPClip;
import planning.RPPart;

/**
 * This interface represents a class to link a {@link RPPart} to its corresponding {@link RPClip}.
 */
public interface RPClipLinker {

	/**
	 * This method links the given {@link RPPart} to the given {@link RPClip}.
	 *
	 * @param clip the {@link RPClip} to link
	 * @param part the {@link RPPart} to link
	 */
	void addClipReferences(RPClip clip, RPPart part);

	/**
	 * A method that returns the {@link RPClip} linked to the given {@link RPPart}.
	 *
	 * @param part the {@link RPPart} linked
	 * @return the {@link RPClip} linked
	 */
	RPClip getClipFromPart(RPPart part);

	/**
	 *
	 * @param clip the {@link RPClip} linked
	 * @return the {@link RPPart} linked
	 */
	RPPart getPartFromClip(RPClip clip);

	/**
	 * A method that returns the {@link RPPart} with the given title.
	 *
	 * @param title the title of the {@link RPPart}
	 * @return the {@link RPPart} with the given title
	 */
	RPPart getPart(String title);

	/**
	 * This method is used to check if a Clip with the given title exists.
	 *
	 * @param title the title of the Clip that need to be checked
	 * @return true if the Clip exists, false otherwise
	 */
	boolean clipExists(String title);

	/**
	 * This method removes the Clip with the given {@link RPPart}.
	 *
	 * @param part the {@link RPPart} of the Clip
	 */
	void removeClip(RPPart part);
}
