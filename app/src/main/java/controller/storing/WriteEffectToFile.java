package controller.storing;

import daw.core.audioprocessing.RPEffect;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class WriteEffectToFile implements WriteToFile<RPEffect> {

    private final CSVPrinter printer;
    private final FileWriter writer;

    public WriteEffectToFile(File file) {
        try {
            this.writer = new FileWriter(Objects.requireNonNull(file));
            this.printer = CSVFormat.Builder.create().setHeader("Parameter,Value").build().print(this.writer);
        } catch (IOException e) {
            throw new IllegalArgumentException("The given file cannot be used to save information.");
        }
    }

    @Override
    public void write(RPEffect element) throws IOException {
        for (final var entry: element.getParameters().entrySet()) {
            this.printer.printRecord(entry.getKey(), entry.getValue());
        }
        this.writer.close();
    }

}
