package planning;

public interface TextFactory {
	Text createFromString(Double size, String content);
	Text createFromText(Double size, String fileName);
}
