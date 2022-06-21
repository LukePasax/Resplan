package controller.storing;

import com.google.gson.JsonIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Implementation of {@link Writer} specific for text files, such as .txt and .json files.
 */
public class RPFileWriter implements Writer {

    private final FileWriter fw;

    /**
     * Constructs a file writer. Calling the {@link #write(String)} method on this object will write data onto
     * the file given as input of this constructor.
     * @param file a {@link File} to write.
     */
    public RPFileWriter(final File file) {
        try {
            this.fw = new java.io.FileWriter(Objects.requireNonNull(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("The given file cannot be used to save information.");
        }
    }

    /**
     * {@inheritDoc}
     * @param data {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public void write(final String data) throws IOException {
        try {
            this.fw.write(data);
            this.fw.close();
        } catch (JsonIOException exception) {
            throw new IOException("Cannot write to file.");
        }
    }

}
