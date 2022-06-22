package planning;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * This is the real implementation of the {@link planning.RPRole} of type "EFFECTS"
 */
public final class EffectsRole extends RoleImpl {
	
	/**
	 * Builds a role of type "EFFECTS" with a description
	 * 
	 * @param title
	 * the title of the role
	 * 
	 * @param description
	 * the description of the role
	 */
	public EffectsRole(final String title, final String description) {
		super(title, description, RoleType.EFFECTS);
	}
	
	/**
	 * Builds a role of type "EFFECTS" without a description
	 * 
	 * @param title
	 * the title of the role
	 */
	public EffectsRole(@JsonProperty(value = "title") final String title) {
		super(title, RoleType.EFFECTS);
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
