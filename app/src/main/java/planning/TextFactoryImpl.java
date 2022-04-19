package planning;

import java.io.BufferedReader;
import java.io.FileReader;

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
				String content = "";
				String line = null;
				final BufferedReader br = new BufferedReader(new FileReader(fileName));
				while((line = br.readLine()) != null) {
					content = content + line;
				}
				br.close();
				return content;
			}
		};
	}

}
