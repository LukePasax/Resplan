package view.planning;

import resplan.Starter;
import java.io.File;
import java.io.IOException;

public final class ClipTextEditorController extends TextEditorController {

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
    protected void onUploadFromFile(final File file) throws IOException {
        Starter.getController().setClipTextFromFile(this.title, file.getAbsolutePath());
        this.setTextArea();
    }

}
