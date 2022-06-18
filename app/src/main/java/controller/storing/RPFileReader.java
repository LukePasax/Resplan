package controller.storing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Implementation of {@link Reader} specific for text files, such as .txt and .json files.
 */
public class RPFileReader implements Reader {

    private final File file;

    /**
     * Constructs a file reader. Calling the {@link #read()} method on this object will read all data from
     * the file given as input of this constructor.
     * @param file a {@link File} to read.
     */
    public RPFileReader(File file) {
        this.file = Objects.requireNonNull(file);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IOException {@inheritDoc}
     */
    @Override
    public String read() throws IOException {
        return Files.readString(Paths.get(this.file.getPath()));
    }

}
