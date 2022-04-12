package planning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextFromFile extends TextImpl {
	
	private BufferedReader br;
	private String content = "";
	private String fileName;
	
	public TextFromFile(final int red, final int green, final int blue, final double size, final String fileName) {
		super(red, green, blue, size);
		this.fileName = fileName;
	}

	public TextFromFile(final String hexaColor, final double size, final String fileName) {
		super(hexaColor, size);
		this.fileName = fileName;
	}

	@Override
	protected String generateContent() throws IOException {

		br = new BufferedReader(new FileReader(this.fileName));
		String line = null;
		while((line = br.readLine()) != null) {
			this.content = this.content + line;
		}
		return this.content;
	}

}
