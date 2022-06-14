package view.effects;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import view.common.App;
import view.common.ViewDataImpl.Effect;

public class EffectsPane extends HBox {
	
	private final Map<String, Class<? extends Pane>> effectsType = createEffects();
	private final Map<Effect, Node> effects = new HashMap<>();

	public EffectsPane(final String channel) {
		App.getData().getChannel(channel).addFxListListener(new ListChangeListener<Effect>() {

			@Override
			public void onChanged(Change<? extends Effect> c) {
				c.next();
				if(c.wasAdded()) {
					c.getAddedSubList().forEach(e -> addEffect(e));
				}
				
				if(c.wasRemoved()) {
					c.getAddedSubList().forEach(e -> removeEffect(e));
				}
				
				if(c.wasPermutated()) {
					c.getAddedSubList().forEach(e -> setEffect(c.getPermutation(c.getList().indexOf(e)), e));
				}
			}
			
		});
	}
	
	private Map<String, Class<? extends Pane>> createEffects(){
		Map<String, Class<? extends Pane>> effects = new HashMap<>();
		effects.put("Compressor", CompressorPane.class);
		effects.put("Limiter", LimiterPane.class);
		effects.put("Pass", PassPane.class);
		effects.put("Reverb", ReverbPane.class);
		return effects;
	}
	
	private void addEffect(final Effect effect) {
		try {
			effects.put(effect, effectsType.get(effect.getType()).getDeclaredConstructor().newInstance());
			this.getChildren().add(effects.get(effect));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private void removeEffect(final Effect effect) {
		effects.remove(effect);
		this.getChildren().remove(effects.get(effect));
	}
	
	private void setEffect(final int newPos, Effect effect) {
		this.getChildren().set(newPos, effects.get(effect));
	}
}
