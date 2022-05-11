package controller.storing;

import com.google.gson.JsonIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class WriteToFileImpl implements WriteToFile {

    private FileWriter fw;

    public WriteToFileImpl(File file) {
        try {
            this.fw = new FileWriter(Objects.requireNonNull(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("The given file cannot be used to save information.");
        }
    }

    @Override
    public void write(String data) throws IOException {
        try {
            this.fw.write(data);
            this.fw.close();
        } catch (JsonIOException exception) {
            throw new IOException("Cannot write to file.");
        }
    }

}
