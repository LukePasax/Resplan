package storing;

import controller.general.Controller;
import controller.storing.ReadFromFile;
import controller.storing.ReadFromFileImpl;
import controller.storing.WriteToFile;
import controller.storing.WriteToFileImpl;
import controller.storing.deserialization.AbstractJacksonDeserializer;
import controller.storing.deserialization.ManagerDeserializer;
import controller.storing.serialization.AbstractJacksonSerializer;
import controller.storing.serialization.ManagerSerializer;
import daw.manager.Manager;
import org.junit.jupiter.api.Test;
import planning.EffectsRole;
import planning.RPRole;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class TestReadAndWrite {

    private static final String FILENAME = "trial.json";

    @Test
    public void simpleWriteAndRead() {
        final Manager man = new Manager();
        AbstractJacksonSerializer<Manager> serializer = new ManagerSerializer(true, false);
        WriteToFile writer = new WriteToFileImpl(new File(Controller.WORKING_DIRECTORY + Controller.SEP + FILENAME));
        AbstractJacksonDeserializer<Manager> deserializer = new ManagerDeserializer();
        ReadFromFile reader = new ReadFromFileImpl(new File(Controller.WORKING_DIRECTORY + Controller.SEP + FILENAME));
        try {
            writer.write(serializer.serialize(man));
            final var manAfterRead = deserializer.deserialize(reader.read());
            assertEquals(man, manAfterRead);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void writeComplex() {
        final Manager man = new Manager();
        RPRole role = new EffectsRole("claps");
        man.addChannel(RPRole.RoleType.EFFECTS, "claps", Optional.empty());
        man.createGroup("effects", RPRole.RoleType.EFFECTS);
        man.addToGroup(role,"effects");
        AbstractJacksonSerializer<Manager> serializer = new ManagerSerializer(true, false);
        WriteToFile writer = new WriteToFileImpl(new File(Controller.WORKING_DIRECTORY + Controller.SEP + FILENAME));
        try {
            writer.write(serializer.serialize(man));
        } catch (IOException e) {
            fail();
        }
    }

}
