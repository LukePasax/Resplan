package planning;

import java.io.IOException;

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
	
	protected abstract String generateContent() throws IOException;
	
	@Override
	public void setContent() {
		try {
			this.content = this.generateContent();
		} catch(IOException e) {
			System.out.println(e.toString());
		}
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
