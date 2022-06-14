package view.common;

import Resplan.Starter;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view.common.ViewDataImpl.Channel;

public abstract class ChannelInfosView extends Pane {
	
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
		this.getChildren().add(new VBox(new Label(ch.getTitle()), infos));
		//set context menu
		MenuItem remove = new MenuItem("Remove");
		remove.setOnAction(a->Starter.getController().deleteChannel(this.ch.getTitle()));
		ContextMenu menu = new ContextMenu(remove);
		this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
	}
	
	public abstract Node drawChannelInfos(Channel ch);

	public Channel getChannel() {
		return this.ch;
	}
}

