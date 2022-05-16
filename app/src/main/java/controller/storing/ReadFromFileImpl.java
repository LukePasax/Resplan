package controller.storing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ReadFromFileImpl implements ReadFromFile {

    private final File file;

    public ReadFromFileImpl(File file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public String read() throws IOException {
        return Files.readString(Paths.get(this.file.getPath()));
    }

}
