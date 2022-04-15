package planning;

import java.util.Optional;

public class SoundtrackPart extends PartImpl {

	public SoundtrackPart(String title, String description) {
		super(title, description, PartType.SOUNDTRACK);
	}

	@Override
	public void addText(Text text) {
		
	}

	@Override
	public boolean isTextPresent() {
		return false;
	}

	@Override
	public Optional<Text> getText() {
		return Optional.empty();
	}

}
