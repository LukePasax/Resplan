package planning;

public interface TextFactory {
	Text createFromString(int size, String content);
	Text createFromFile(int size, String fileName);
}
