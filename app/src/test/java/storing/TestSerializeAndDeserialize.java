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
import daw.manager.ImportException;
import daw.manager.Manager;
import org.junit.jupiter.api.Test;
import planning.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class TestSerializeAndDeserialize {

    private static final String FILENAME = Controller.WORKING_DIRECTORY + Controller.SEP + "trial.json";

    @Test
    public void simpleSerializationAndDeserialization() {
        final Manager man = new Manager();
        AbstractJacksonSerializer<Manager> serializer = new ManagerSerializer(true, false);
        WriteToFile writer = new WriteToFileImpl(new File(FILENAME));
        AbstractJacksonDeserializer<Manager> deserializer = new ManagerDeserializer();
        ReadFromFile reader = new ReadFromFileImpl(new File(FILENAME));
        try {
            writer.write(serializer.serialize(man));
            final var manAfterRead = deserializer.deserialize(reader.read());
            System.out.println(serializer.serialize(manAfterRead));
            //assertEquals(man, manAfterRead);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void simpleRead() {
        Manager man = new Manager();
        try {
            man = new ManagerDeserializer()
                    .deserialize(new ReadFromFileImpl(new File(FILENAME))
                    .read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new ManagerSerializer(true, false).serialize(man));
    }

    @Test
    public void serializationAndDeserializationWithNewClip() {
        final Manager man = new Manager();
        RPRole role = new EffectsRole("claps");
        man.addChannel(RPRole.RoleType.EFFECTS, "claps", Optional.of("ciao"));
        man.createGroup("myStuff", RPRole.RoleType.EFFECTS);
        man.addToGroup(role,"myStuff");
        try {
            File file = new File(Controller.WORKING_DIRECTORY + Controller.SEP + "src" + Controller.SEP +
                    "test" + Controller.SEP + "resources" + Controller.SEP + "audio" + Controller.SEP +
                    "Alergy - Brain in the Jelly.wav");
            man.addClip(RPPart.PartType.EFFECTS,"part1", Optional.empty(),
                    "claps",0.00, 0.20,Optional.empty());
            man.addClip(RPPart.PartType.EFFECTS, "part2", Optional.empty(),
                    "claps", 10.00, 0.50, Optional.of(file));
        } catch (ImportException e) {
            e.printStackTrace();
        }
        AbstractJacksonSerializer<Manager> serializer = new ManagerSerializer(true, false);
        WriteToFile writer = new WriteToFileImpl(new File(FILENAME));
        AbstractJacksonDeserializer<Manager> deserializer = new ManagerDeserializer();
        ReadFromFile reader = new ReadFromFileImpl(new File(FILENAME));
        try {
            writer.write(serializer.serialize(man));
            final var manAfterRead = deserializer.deserialize(reader.read());
            System.out.println(serializer.serialize(manAfterRead));
        } catch (IOException e) {
            fail();
        }
    }

}
