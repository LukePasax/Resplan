package planning;

import java.io.BufferedReader;
import java.io.FileReader;

public class TextFactoryImpl implements TextFactory {

	@Override
	public Text createFromString(final int size, final String content) {
		return new Text() {
			
			@Override
			public int getSize() {
				return size;
			}
			
			@Override
			public String getContent() {
				return content;
			}
		};
	}

	@Override
	public Text createFromFile(final int size, final String fileName) {
		return new Text() {
			
			@Override
			public int getSize() {
				return size;
			}
			
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
