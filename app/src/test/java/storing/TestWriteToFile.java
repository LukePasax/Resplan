package storing;

import controller.storing.WriteToFile;
import controller.storing.WriteToFileImpl;
import controller.storing.serialization.AbstractJacksonSerializer;
import controller.storing.serialization.ManagerSerializer;
import daw.manager.Manager;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class TestWriteToFile {

    private static final String DIR = System.getProperty("user.dir");
    private static final String SEP = System.getProperty("file.separator");

    @Test
    public void writeWithJackson() {
        final Manager man = new Manager();
        AbstractJacksonSerializer<Manager> serializer = new ManagerSerializer(true, false);
        WriteToFile writer = new WriteToFileImpl(new File(DIR + SEP + "trial.json"));
        try {
            writer.write(serializer.serialize(man));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
