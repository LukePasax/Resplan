package view.edit;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import view.common.ViewDataImpl.Clip;

public class EditClipView extends BorderPane {

	Clip clip;

	public EditClipView(Clip clip) {
		this.clip = clip;
		ImageView wave = new ImageView(new Image("/images/audioWave.png"));
		Label title = new Label(clip.getTitle());
		wave.setFitWidth(30);
		wave.setFitHeight(20);
		//add all to view
		this.setTop(title);
		this.setCenter(wave);
	}
}
