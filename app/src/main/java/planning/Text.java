package planning;


/**
 *It's an interface modelling a text that could be used in a RPRole of type speech
 */
public interface Text {
	/*Template method
	public void setContent(final String content);
	public void setContentFromFile(final String file);
	*/
	String getContent();
	void setColor(Color color);
	Color getColor();
	void setSize(Double size);
	Double getSize();
}
