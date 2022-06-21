package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPPart} of type "SOUNDTRACK"
 */
public final class SoundtrackPart extends PartImpl {
	
	/**
	 * Builds a part of type "SOUNDTRACK" with a description
	 * 
	 * @param title
	 * the title of the part
	 * 
	 * @param description
	 * the description of the part
	 */
	public SoundtrackPart(String title, String description) {
		super(title, description, PartType.SOUNDTRACK);
	}
	
	/**
	 * Builds a part of type "SOUNDTRACK" without a description
	 * 
	 * @param title
	 * the title of the part
	 */
	public SoundtrackPart(String title) {
		super(title, PartType.SOUNDTRACK);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addText(Text text) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isTextPresent() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Optional<Text> getText() {
		return Optional.empty();
	}

}
