package view.effects;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class EffectsPane extends VBox {

	static int oldpos;
	static Pair<BorderPane, String> current;
	static Pair<BorderPane, String> currentCycle;
	static int index;
	
	public EffectsPane() throws IOException {
		
		Map<Pair<BorderPane, String>, Pair<Integer, Boolean>> panesmap = new HashMap<>();
		
		final HBox root = new HBox();
		final CompressorPane cpane = new CompressorPane();
		final LimiterPane lpane = new LimiterPane();
		final PassPane ppane = new PassPane();
		final ReverbPane rpane = new ReverbPane();
		
		final Button set = new Button("Set");
		
		final Pair<BorderPane, String> compressor = new Pair<>(cpane, "Compressor");
		final Pair<BorderPane, String> limiter = new Pair<>(lpane, "Limiter");
		final Pair<BorderPane, String> pass = new Pair<>(ppane, "Pass");
		final Pair<BorderPane, String> reverb = new Pair<>(rpane, "Reverb");
		
		panesmap.put(compressor, new Pair<Integer, Boolean>(1, false));
		panesmap.put(limiter, new Pair<Integer, Boolean>(2, false));
		panesmap.put(pass, new Pair<Integer, Boolean>(3, false));
		panesmap.put(reverb, new Pair<Integer, Boolean>(4, false));
		
		final ComboBox<String> panes = new ComboBox<>(FXCollections.observableArrayList("Compressor", "Limiter", "Pass", "Reverb"));
		panes.setPromptText("Choose an effect");
		
		final ComboBox<Integer> position = new ComboBox<>(FXCollections.observableArrayList(1, 2, 3, 4));
		position.setPromptText("Choose a position");
		
		root.getChildren().addAll(cpane, lpane, ppane, rpane);
		
		
		set.setOnMouseClicked(e -> {
			int newpos = position.getSelectionModel().getSelectedItem();
			panesmap.forEach((p, i) -> {
				if(p.getValue().equals(panes.getSelectionModel().getSelectedItem())) {
					oldpos = i.getKey();
					current = p;
				}
			});
			if(newpos < oldpos) {
				for(index = newpos; index < oldpos; index++) {
					panesmap.forEach((p, i) -> {
						if(i.getKey() == index && !i.getValue()) {
							currentCycle = p;
						}
					});
					panesmap.replace(currentCycle, new Pair<Integer, Boolean>(index, false), new Pair<Integer, Boolean>(index+1, true));
				}
				panesmap.replace(current, new Pair<Integer, Boolean>(newpos, true));
				panesmap.forEach((p, i) -> {
					panesmap.replace(p, new Pair<Integer, Boolean>(i.getKey(), false));
				});
			}

			root.getChildren().removeAll(ppane,lpane,cpane,rpane);
			root.getChildren().addAll(new BorderPane(),new BorderPane(),new BorderPane(),new BorderPane());

			panesmap.forEach((p, i) -> {
				root.getChildren().set(i.getKey()-1, p.getKey());
			});			
		});
		
		this.getChildren().addAll(panes, position, set, root);		
	}
}
