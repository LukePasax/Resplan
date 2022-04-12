package planning;

public abstract class TextImpl implements Text {
	
	private Color color;
	private Double size;
	private ColorFactory cf = new ColorFactoryImpl();
	private String content;

	public TextImpl(final int red, final int green, final int blue, final double size) {
		this.color = cf.createColorFromRGB(red, green, blue);
		this.size = size;
	}
	
	public TextImpl(final String hexaColor, final double size) {
		this.color = cf.createColorFromHexadecimal(hexaColor);
		this.size = size;
	}
	
	protected abstract String generateContent();
	
	@Override
	public void setContent() {
		this.content = this.generateContent();
	}
	
	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public Double getSize() {
		return this.size;
	}

}
