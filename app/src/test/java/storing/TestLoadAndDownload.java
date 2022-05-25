package storing;

import controller.general.Controller;
import controller.general.ControllerImpl;
import controller.general.DownloadingException;
import controller.general.LoadingException;
import controller.storing.WriteToFileImpl;
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
        this.controller = new ControllerImpl();
    }

    @Test
    public void testSave() {
        try {
            // current project has never been saved before, therefore an IllegalStateException has to be raised
            this.controller.saveCurrentProject();
            fail();
        } catch (IllegalStateException e) {
            try {
                // after the project has been saved, it can be opened again
                final var manBeforeSave = this.controller.getManager();
                final var file = new File(DIR + SEP + FILENAME);
                this.controller.saveWithName(file);
                // new run of the application
                this.controller = new ControllerImpl();
                // opens the project that had been saved before
                this.controller.openProject(file);
                final var manAfterSave = this.controller.getManager();
                new WriteToFileImpl(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "prova10.json"))
                        .write(new ManagerSerializer(true, false).serialize(manBeforeSave));
                new WriteToFileImpl(new File(Controller.WORKING_DIRECTORY + Controller.SEP + "prova11.json"))
                        .write(new ManagerSerializer(true, false).serialize(manAfterSave));
                assertEquals(manBeforeSave, manAfterSave);
            } catch (DownloadingException | LoadingException ex) {
                ex.printStackTrace();
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
            this.controller.openProject(new File(DIR + SEP + FILENAME));
            try {
                this.controller.setTemplateProject();
                fail();
            } catch (IllegalStateException ex) {
                this.controller.saveWithName(new File(DIR + SEP + FILENAME));
            }
            final var manBeforeSave = this.controller.getManager();
            this.controller = new ControllerImpl();
            final var manAfterSave = this.controller.getManager();
            assertEquals(manBeforeSave, manAfterSave);
        } catch (LoadingException e) {
            e.printStackTrace();
        } catch (DownloadingException e) {
            e.printStackTrace();
        }
    }

}
