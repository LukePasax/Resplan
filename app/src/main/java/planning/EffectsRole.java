package planning;

import java.util.Optional;

public class EffectsRole extends RoleImpl {

	public EffectsRole(final String title, final String description) {
		super(title, description, RoleType.EFFECTS);
	}

	@Override
	public void addSpeaker(Speaker speaker) {
		
	}

	@Override
	public Optional<Speaker> getSpeaker() {
		return Optional.empty();
	}

}
