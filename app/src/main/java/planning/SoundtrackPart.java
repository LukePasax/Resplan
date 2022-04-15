package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPPart} of type "SOUNDTRACK"
 */
public class SoundtrackPart extends PartImpl {
	
	/**
	 * Builds a part of type "SOUNDTRACK"
	 * 
	 * @param title
	 * 			the title of the part
	 * 
	 * @param description
	 * 			the description of the part
	 */
	public SoundtrackPart(String title, String description) {
		super(title, description, PartType.SOUNDTRACK);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addText(Text text) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTextPresent() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Text> getText() {
		return Optional.empty();
	}

}
