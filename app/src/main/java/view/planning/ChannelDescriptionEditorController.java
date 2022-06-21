package view.planning;

import Resplan.Starter;
import controller.storing.RPFileReader;

import java.io.File;
import java.io.IOException;

public class ChannelDescriptionEditorController extends TextEditorController {

    @Override
    protected void setPromptText() {
        this.textArea.setPromptText("Insert channel description here...");
    }

    @Override
    protected void setTextArea() {
        Starter.getController().getChannelDescription(this.title).ifPresent(s -> this.textArea.setText(s));
    }

    @Override
    protected void onSave() {
        Starter.getController().setChannelDescription(this.title, this.textArea.getText());
    }

    @Override
    protected void onUploadFromFile(File file) throws IOException {
        Starter.getController().setChannelDescription(this.title, new RPFileReader(file).read());
        this.setTextArea();
    }

}
