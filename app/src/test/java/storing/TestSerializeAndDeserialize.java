package storing;

import controller.general.Controller;
import controller.storing.Reader;
import controller.storing.RPFileReader;
import controller.storing.Writer;
import controller.storing.RPFileWriter;
import controller.storing.deserialization.ManagerDeserializer;
import controller.storing.serialization.ManagerSerializer;
import daw.core.audioprocessing.BasicProcessingUnitBuilder;
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
        ManagerSerializer serializer = new ManagerSerializer();
        Writer writer = new RPFileWriter(new File(FILENAME));
        ManagerDeserializer deserializer = new ManagerDeserializer();
        Reader reader = new RPFileReader(new File(FILENAME));
        try {
            writer.write(serializer.serialize(man));
            final var manAfterRead = deserializer.deserialize(reader.read());
            assertEquals(new ManagerSerializer().serialize(man),
                    new ManagerSerializer().serialize(manAfterRead));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void complexSerializationAndDeserialization() {
        final Manager man = new Manager();
        man.addChannel(RPRole.RoleType.EFFECTS, "claps", Optional.of("ciao"));
        man.addChannel(RPRole.RoleType.EFFECTS, "myStuff", Optional.of("what do i know"));
        man.getChannelFromTitle("myStuff").addProcessingUnit(new BasicProcessingUnitBuilder()
                .limiter(1)
                .gate(1)
                .highPassFilter(1)
                .build());
        man.addChannel(RPRole.RoleType.SPEECH, "Giacomo", Optional.empty());
        man.addSection("first", Optional.of("ciao"), 0.10, 0.0);
        man.addSection("second", Optional.of("bella"), 0.50, 0.0);
        final var speaker1 = new SimpleSpeaker(1, "Giacomo", "Sirri");
        final var speaker2 = new SimpleSpeaker(2, "Luca", "Pasini");
        man.addSpeakerToRubric(speaker1);
        man.addSpeakerToRubric(speaker2);
        man.removeSpeakerFromRubric(speaker1);
        try {
            File file = new File(Controller.WORKING_DIRECTORY + Controller.SEP + "src" + Controller.SEP +
                    "test" + Controller.SEP + "resources" + Controller.SEP + "audio" + Controller.SEP +
                    "Alergy - Brain in the Jelly.wav");
            man.addClip(RPPart.PartType.EFFECTS,"part1", Optional.of("ciao"),
                    "claps",0.00, 0.20,Optional.empty());
            man.addClip(RPPart.PartType.EFFECTS, "part2", Optional.empty(),
                    "claps", 10.00, 0.50, Optional.of(file));
        } catch (ImportException e) {
            e.printStackTrace();
        }
        ManagerSerializer serializer = new ManagerSerializer();
        Writer writer = new RPFileWriter(new File(FILENAME));
        ManagerDeserializer deserializer = new ManagerDeserializer();
        Reader reader = new RPFileReader(new File(FILENAME));
        try {
            writer.write(serializer.serialize(man));
            final var manAfterRead = deserializer.deserialize(reader.read());
            assertEquals(serializer.serialize(manAfterRead), new RPFileReader(new File(FILENAME)).read());
        } catch (IOException e) {
            fail();
        }
    }

}
