package view.effects;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EffectsPane extends VBox {

	public EffectsPane() {
		Map<String, Pane> effects = new HashMap<>();
		effects.put("Compressor", new CompressorPane());
		effects.put("Limiter", new LimiterPane());
		effects.put("Pass", new PassPane());
		effects.put("Reverb", new ReverbPane());
		ObservableList<String> addFX = FXCollections.observableArrayList("Compressor", "Limiter", "Pass", "Reverb");
		ObservableList<String> rmFX = FXCollections.observableArrayList();
		ComboBox<String> addEffects = new ComboBox<>(addFX);
		Button add = new Button("Add effect");
		ComboBox<String> removeEffects = new ComboBox<>(rmFX);
		Button remove = new Button("Remove effect");
		this.getChildren().addAll(addEffects, add, removeEffects, remove);
		add.setOnMouseClicked(e -> {
			var element = addEffects.getSelectionModel().getSelectedItem();
			this.getChildren().add(effects.get(element));
			addFX.remove(element);
			rmFX.add(element);
		});
		remove.setOnMouseClicked(e -> {
			var element = removeEffects.getSelectionModel().getSelectedItem();
			this.getChildren().remove(effects.get(element));
			addFX.add(element);
			rmFX.remove(element);
		});
	}
}
