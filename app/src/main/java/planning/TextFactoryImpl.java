package planning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextFactoryImpl implements TextFactory {

	@Override
	public Text createFromString(Double size, String content) {
		return new Text() {
			
			@Override
			public Double getSize() {
				return size;
			}
			
			@Override
			public String getContent() {
				return content;
			}
		};
	}

	@Override
	public Text createFromText(Double size, String fileName) {
		return new Text() {
			
			@Override
			public Double getSize() {
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
