package planning;

/**
 *It's an interface modelling a text that could be used in a RPRole of type speech
 */
public interface Text {
	String getContent() throws Exception;
	Double getSize();
}
