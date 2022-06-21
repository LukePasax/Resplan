package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPPart} of type "EFFECTS"
 */
public final class EffectsPart extends PartImpl {
	
	/**
	 * Builds a part of type "EFFECTS" with a description
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
	 * Builds a part of type "EFFECTS" without a description
	 * 
	 * @param title
	 * the title of the part
	 */
	public EffectsPart(String title) {
		super(title, PartType.EFFECTS);
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
