package planning;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This is the implementation of a {@link planning.TextFactory}
 */
public class TextFactoryImpl implements TextFactory {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Text createFromString(final String content) {
		return new Text() {
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public String getContent() {
				return content;
			}
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Text createFromFile(final String fileName) {
		return new Text() {
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public String getContent() throws Exception {
				Path filePath = Path.of(fileName);
				String content = Files.readString(filePath);
				return content;
			}
		};
	}

}
