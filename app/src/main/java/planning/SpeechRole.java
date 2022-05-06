package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPRole} of type "SPEECH"
 */
public class SpeechRole extends RoleImpl {
	
	private Optional<Speaker> speaker = Optional.empty();
	
	/**
	 * Builds a role of type "SPEECH" with a description
	 * 
	 * @param title
	 * the title of the role
	 * 
	 * @param description
	 * the description of the role
	 */
	public SpeechRole(final String title, final String description) {
		super(title, description, RoleType.SPEECH);
	}
	
	/**
	 * Builds a role of type "SPEECH" without a description
	 * 
	 * @param title
	 * the title of the role
	 */
	public SpeechRole(final String title) {
		super(title, RoleType.SPEECH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSpeaker(Speaker speaker) {
		this.speaker = Optional.of(speaker);		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSpeakerPresent() {
		return this.speaker.isPresent();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Speaker> getSpeaker() {
		return this.speaker;	
	}
}
