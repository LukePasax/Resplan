package view.edit;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import view.common.App;
import view.common.ChannelsView;

public final class FXView {
	
	private String selectedChannel;
	private final HBox fxPanel;

	public FXView(final HBox fxPanel, final ChannelsView chView) {
		chView.addSelectListener(new ChangeListener<String>() {

			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				selectedChannel = newValue;
				changeFXView();
			}
		});
		this.fxPanel = fxPanel;
	}

	private void changeFXView() {
		this.fxPanel.getChildren().clear();
		var fxView = App.getData().getChannel(selectedChannel).getFxView();
		this.fxPanel.getChildren().add(fxView);
		HBox.setHgrow(fxView, Priority.ALWAYS);
		fxPanel.setFillHeight(true);
	}
}
