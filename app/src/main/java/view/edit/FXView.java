package view.edit;

import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import view.common.App;
import view.common.ChannelsView;

public class FXView {
	
	private String selectedChannel;
	private HBox fxPanel;
	private Map<String, Node> fxViews;

	public FXView(HBox fxPanel, ChannelsView chView) {
		chView.addSelectObserver(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				selectedChannel = newValue;
				ChangeFXView();
			}
			
		});
		this.fxPanel = fxPanel;
	}

	private void ChangeFXView() {
		this.fxPanel.getChildren().clear();
		this.fxPanel.getChildren().add(App.getData().getChannel(selectedChannel).getFxView());
	}
}
