package storing;

import com.fasterxml.jackson.core.JsonProcessingException;
import controller.storing.Serializer;
import controller.storing.JacksonSerializer;
import daw.core.channel.BasicChannelFactory;
import daw.core.channel.ChannelFactory;
import daw.core.clip.TapeChannel;
import daw.manager.ChannelLinker;
import daw.manager.ClipLinker;
import daw.manager.Manager;
import org.junit.jupiter.api.Test;
import planning.*;

import javax.sound.sampled.Clip;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

public class TestWriteToFile {

    private static final String SEP = System.getProperty("file.separator");
    private static final String DIR = System.getProperty("user.dir");

    @Test
    public void writeWithGSON() {
        /*
        final var file = new File(DIR + SEP + "trial.json");
        final WriteToFile<Manager> writer = new WriteToJSONFile<>(file);
        System.out.println(file.getPath());
        final Manager tc = new Manager();
        final BasicProcessingUnitBuilder tp = new BasicProcessingUnitBuilder();
        final var pu = tp.compressor(1).lowPassFilter(1, 100.0f).build();
        final var comp = new Compressor(1);
        try {
            final var text = writer.write(channel);
            System.out.println(text);
        } catch (IOException exception) {
            fail();
        }
        try {
            writer.write(tc);
        } catch (IOException e) {
            fail();
        }

         */
    }

    @Test
    public void writeWithJackson() throws JsonProcessingException {
        final Manager man = new Manager();
        final RPRole role = new SpeechRole("Giacomo Sirri");
        //man.addChannel(RPRole.RoleType.SPEECH, "giacomo", Optional.empty());
        //final ChannelFactory cf = new BasicChannelFactory();
        //man.getChannelLinker().addChannelReferences(cf.gated(), new TapeChannel(), role);
        Serializer<RPRole> serializer = new JacksonSerializer<>();
        System.out.println(serializer.serialize(role));
    }

}
