package view.common;

import javafx.stage.FileChooser;
import java.io.File;

public class JsonFilePicker extends FilePicker {

    public JsonFilePicker() {
        super("File JSON","*.json");
    }

}
