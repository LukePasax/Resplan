package view.effects;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.common.App;
import view.common.ViewDataImpl.Effect;

public class EffectsPane extends VBox {
	
	private final Map<String, Pane> effects = createEffects();
	private final EffectsPane rootPane = this;

	public EffectsPane(final String channel) {
		App.getData().getChannel(channel).addFxListListener(new ListChangeListener<Effect>() {

			@Override
			public void onChanged(Change<? extends Effect> c) {
				c.next();
				if(c.wasAdded()) {
					c.getAddedSubList().forEach(e -> rootPane.getChildren().add(effects.get(e.getType())));
				}
				
				if(c.wasRemoved()) {
					c.getAddedSubList().forEach(e -> rootPane.getChildren().remove(effects.get(e.getType())));
				}
				
				if(c.wasPermutated()) {
					c.getAddedSubList().forEach(e -> {
						rootPane.getChildren().set(rootPane.getChildren().indexOf(effects.get(e.getType())), effects.get(e.getType()));
					});
				}
			}
			
		});
	}
	
	private Map<String, Pane> createEffects(){
		Map<String, Pane> effects = new HashMap<>();
		effects.put("Compressor", new CompressorPane());
		effects.put("Limiter", new LimiterPane());
		effects.put("Pass", new PassPane());
		effects.put("Reverb", new ReverbPane());
		return effects;
	}
}
