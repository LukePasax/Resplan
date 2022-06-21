package view.planning;

import Resplan.Starter;
import java.io.File;
import java.io.IOException;

public class ClipTextEditorController extends TextEditorController {

    @Override
    protected void setPromptText() {
        this.textArea.setPromptText("Insert clip text here...");
    }

    @Override
    protected void setTextArea() {
        Starter.getController().getClipText(this.title).ifPresent(s -> this.textArea.setText(s));
    }

    @Override
    protected void onSave() {
        Starter.getController().setClipText(this.title, this.textArea.getText());
    }

    @Override
    protected void onUploadFromFile(File file) throws IOException {
        Starter.getController().setClipTextFromFile(this.title, file.getAbsolutePath());
        this.setTextArea();
    }

}