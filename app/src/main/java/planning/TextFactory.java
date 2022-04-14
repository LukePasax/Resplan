package planning;

public interface TextFactory {
	Text createFromString(Double size, String content);
	Text createFromFile(Double size, String fileName);
}
