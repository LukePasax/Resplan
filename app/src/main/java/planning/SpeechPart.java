package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPPart} of type "SPEECH"
 */
public final class SpeechPart extends PartImpl {
	
	private Optional<Text> text = Optional.empty();
	
	/**
	 * Builds a part of type "SPEECH" with a description
	 * 
	 * @param title
	 * the title of the part
	 * 
	 * @param description
	 * the description of the part
	 */
	public SpeechPart(String title, String description) {
		super(title, description, PartType.SPEECH);
	}
	
	/**
	 * Builds a part of type "SPEECH" without a description
	 * 
	 * @param title
	 * the title of the part
	 */
	public SpeechPart(String title) {
		super(title, PartType.SPEECH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addText(final Text text) {
		this.text = Optional.of(text);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTextPresent() {
		return this.text.isPresent();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Optional<Text> getText() {
		return this.text;
	}

}
