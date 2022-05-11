package controller.storing;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class WriteToJsonFile<T> implements WriteToFile<T> {

    private final Gson writer = new Gson();
    private FileWriter fw;

    public WriteToJsonFile(File file) {
        try {
            this.fw = new FileWriter(Objects.requireNonNull(file));
        } catch (IOException e) {
            throw new IllegalArgumentException("The given file cannot be used to save information.");
        }
    }

    @Override
    public void write(T element) throws IOException {
        try {
            this.writer.toJson(element, fw);
            this.fw.close();
        } catch (JsonIOException exception) {
            throw new IOException("Cannot write to file.");
        }
    }

}
