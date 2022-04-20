package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPPart} of type "EFFECTS"
 */
public class EffectsPart extends PartImpl {
	
	/**
	 * Builds a part of type "EFFECTS"
	 * 
	 * @param title
	 * the title of the part
	 * 
	 * @param description
	 * the description of the part
	 */
	public EffectsPart(String title, String description) {
		super(title, description, PartType.EFFECTS);
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
