package planning;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPRole} of type "SOUNDTRACK"
 */
public final class SoundtrackRole extends RoleImpl {
	
	/**
	 * Builds a role of type "SOUNDTRACK" with a description
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
	 * Builds a role of type "SOUNDTRACK" without a description
	 * 
	 * @param title
	 * the title of the role
	 */
	public SoundtrackRole(@JsonProperty(value = "title") final String title) {
		super(title, RoleType.SOUNDTRACK);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addSpeaker(Speaker speaker) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isSpeakerPresent() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Optional<Speaker> getSpeaker() {
		return Optional.empty();
	}
}
