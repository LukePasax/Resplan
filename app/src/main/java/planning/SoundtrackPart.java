package planning;

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
	public Text getText() {
		return null;
	}

}
