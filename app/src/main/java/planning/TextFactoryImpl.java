package planning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is the implementation of a {@link planning.TextFactory}
 */
public final class TextFactoryImpl implements TextFactory {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Text createFromString(final String content) {
		return () -> content;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Text createFromFile(final String fileName) throws IOException {
		return this.createFromString(Files.readString(Path.of(fileName)));
	}

}
