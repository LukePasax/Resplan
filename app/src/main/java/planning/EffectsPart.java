package planning;

public class EffectsPart extends PartImpl {

	public EffectsPart(String title, String description) {
		super(title, description, PartType.EFFECTS);
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
