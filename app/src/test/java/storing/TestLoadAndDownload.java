package storing;

import controller.general.Controller;
import controller.general.ControllerImpl;
import controller.general.DownloadingException;
import controller.general.LoadingException;
import controller.storing.RPFileWriter;
import controller.storing.serialization.ManagerSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestLoadAndDownload {

    private static final String DIR = System.getProperty("user.dir");
    private static final String SEP = System.getProperty("file.separator");
    private static final String FILENAME = "current.json";
    private static ControllerImpl controller;

    @BeforeEach
    public void initializeController() {
        controller = new ControllerImpl();
    }

    @Test
    public void testSave() {
        try {
            // current project has never been saved before, therefore an IllegalStateException has to be raised
            controller.save();
            fail();
        } catch (IllegalStateException e) {
            try {
                // after the project has been saved, it can be opened again
                final var manBeforeSave = controller.getManager();
                final var file = new File(DIR + SEP + FILENAME);
                controller.saveWithName(file);
                // new run of the application
                controller = new ControllerImpl();
                // opens the project that had been saved before
                controller.openProject(file);
                final var manAfterSave = controller.getManager();
                new RPFileWriter(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "trial1.json"))
                        .write(new ManagerSerializer().serialize(manBeforeSave));
                new RPFileWriter(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "trial2.json"))
                        .write(new ManagerSerializer().serialize(manAfterSave));
                assertEquals(manBeforeSave, manAfterSave);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (DownloadingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTemplate() {
        try {
            controller.openProject(new File(DIR + SEP + FILENAME));
            try {
                controller.setTemplateProject();
                fail();
            } catch (IllegalStateException ex) {
                controller.saveWithName(new File(DIR + SEP + FILENAME));
            }
            final var manBeforeSave = controller.getManager();
            controller = new ControllerImpl();
            final var manAfterSave = controller.getManager();
            assertEquals(manBeforeSave, manAfterSave);
        } catch (LoadingException | DownloadingException e) {
            e.printStackTrace();
        }
    }

}
