package planning;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPRole} of type "SOUNDTRACK"
 */
public class SoundtrackRole extends RoleImpl {
	
	/**
	 * Builds a role of type "SOUNDTRACK"
	 * 
	 * @param title
	 * the title of the role
	 * 
	 * @param description
	 * the description of the role
	 */
	public SoundtrackRole(final String title, final String description) {
		super(title, description, RoleType.SOUNDTRACK);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSpeaker(Speaker speaker) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSpeakerPresent() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Speaker> getSpeaker() {
		return Optional.empty();
	}
}
