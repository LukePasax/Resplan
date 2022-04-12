package planning;

public class SimpleText extends TextImpl {
	
	private String content;
	public SimpleText(final int red, final int green, final int blue, final double size, final String content) {
		super(red, green, blue, size);
		this.content = content;
	}

	public SimpleText(final String hexaColor, final double size, final String content) {
		super(hexaColor, size);
		this.content = content;
	}

	@Override
	protected String generateContent() {
		return content;
	}

}
