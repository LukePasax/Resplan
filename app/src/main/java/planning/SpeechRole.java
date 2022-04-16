package planning;

import java.util.Optional;

public class SpeechRole extends RoleImpl {
	
	private Optional<Speaker> speaker;

	public SpeechRole(final String title, final String description) {
		super(title, description, RoleType.SPEECH);
	}

	@Override
	public void addSpeaker(Speaker speaker) {
		this.speaker = Optional.of(speaker);		
	}

	@Override
	public Optional<Speaker> getSpeaker() {
		if(!this.speaker.isPresent()) {
			return Optional.empty();
		}
		return this.speaker;			
	}
}
