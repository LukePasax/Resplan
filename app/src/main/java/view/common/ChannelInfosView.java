package view.common;

import Resplan.Starter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import view.common.ViewDataImpl.Channel;
import view.planning.ChannelDescriptionEditorController;
import view.planning.ClipDescriptionEditorController;

import java.io.IOException;

public abstract class ChannelInfosView extends AnchorPane {
	
	private final static Paint borderColor = Paint.valueOf("#999999");
	
	private final Channel ch;
	private final VBox infos = new VBox();
	
	public ChannelInfosView(Channel ch, Color color) {
		super();
		this.ch = ch;
		//borderLayout
		this.setBorder(new Border(new BorderStroke(borderColor, color, borderColor, color, 
				BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, null, new BorderWidths(0, 6, 1, 0), null)));
		//set content
		infos.getChildren().add(drawChannelInfos(ch));
		Label titleLabel = new Label(ch.getTitle());
		titleLabel.setTextFill(Color.BLACK);
		VBox content = new VBox(titleLabel, infos);
		this.getChildren().add(content);
		AnchorPane.setBottomAnchor(content, 0.0);
		AnchorPane.setTopAnchor(content, 0.0);
		AnchorPane.setRightAnchor(content, 0.0);
		AnchorPane.setLeftAnchor(content, 0.0);
		//set context menu
		MenuItem remove = new MenuItem("Remove");
		remove.setOnAction(a->Starter.getController().deleteChannel(this.ch.getTitle()));
		MenuItem editDescription = new MenuItem("Edit description");
		editDescription.setOnAction(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/TextEditorView.fxml"));
				loader.setController(new ChannelDescriptionEditorController());
				Stage stage = new Stage();
				stage.setScene(new Scene(loader.load()));
				stage.setTitle("Text Editor - " + this.getChannel().getTitle() + " description");
				stage.initOwner(this.getScene().getWindow());
				((ChannelDescriptionEditorController) loader.getController()).setTitle(this.getChannel().getTitle());
				stage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		ContextMenu menu = new ContextMenu(remove, editDescription);
		this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
	}
	
	public abstract Node drawChannelInfos(Channel ch);

	public Channel getChannel() {
		return this.ch;
	}
}

