package planning;

import java.util.Optional;

public class SoundtrackRole extends RoleImpl {

	public SoundtrackRole(final String title, final String description) {
		super(title, description, RoleType.SOUNDTRACK);
	}

	@Override
	public void addSpeaker(Speaker speaker) {
		
	}

	@Override
	public Optional<Speaker> getSpeaker() {
		return Optional.empty();
	}
}
