package storing;

import controller.general.Controller;
import controller.general.ControllerImpl;
import controller.general.DownloadingException;
import controller.storing.RPFileWriter;
import controller.storing.serialization.ManagerSerializer;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestLoadAndDownload {

    private static final String DIR = System.getProperty("user.dir");
    private static final String SEP = System.getProperty("file.separator");
    private static final String FILENAME = "current.json";

    @Test
    public void testSave() {
        final var file = new File(DIR + SEP + FILENAME);
        // resets the eventual template project
        try {
            new RPFileWriter(new File(Controller.WORKING_DIRECTORY + Controller.SEP + Controller.APP_SETTINGS))
                    .write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final var controller = new ControllerImpl();
        try {
            // current project has never been saved before, therefore an IllegalStateException has to be raised
            controller.save();
            fail();
        } catch (IllegalStateException e) {
            try {
                // after the project has been saved, it can be opened again
                final var manBeforeSave = controller.getManager();
                controller.saveWithName(file);
                // new run of the application
                final var newController = new ControllerImpl();
                // opens the project that had been saved before
                newController.openProject(file);
                final var manAfterSave = newController.getManager();
                new RPFileWriter(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "trial1.json"))
                        .write(new ManagerSerializer().serialize(manBeforeSave));
                new RPFileWriter(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "trial2.json"))
                        .write(new ManagerSerializer().serialize(manAfterSave));
                assertEquals(new ManagerSerializer().serialize(manAfterSave),
                        new ManagerSerializer().serialize(manBeforeSave));
                // now it is possible to set a template project
                controller.setTemplateProject();
                final var lastController = new ControllerImpl();
                final var manTemplate = lastController.getManager();
                assertEquals(new ManagerSerializer().serialize(manTemplate),
                        new ManagerSerializer().serialize(manBeforeSave));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (DownloadingException e) {
            e.printStackTrace();
        }
    }

}
