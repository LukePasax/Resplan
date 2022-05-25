package storing;

import controller.general.Controller;
import controller.general.ControllerImpl;
import controller.general.DownloadingException;
import controller.general.LoadingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLoadAndDownload {

    private static final String SEP = System.getProperty("file.separator");
    private static final String DIR = System.getProperty("user.dir");
    private static final String FILENAME = "current.json";

    private static ControllerImpl controller = new ControllerImpl();

    @BeforeEach
    public void initializeController() {
        this.controller = new ControllerImpl();
    }

    @Test
    public void testSave() {
        try {
            this.controller.saveCurrentProject();
        } catch (IllegalStateException e) {
            try {
                this.controller.saveWithName(new File(DIR + SEP + FILENAME));
            } catch (DownloadingException ex) {
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
            this.controller.setTemplateProject();
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
