package view.effects;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import resplan.Starter;
import daw.core.audioprocessing.*;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import view.common.App;
import view.common.ViewDataImpl.Effect;

public final class EffectsPane extends ScrollPane {
	
	private final static Map<String, Class<? extends Pane>> effectsType = createEffects();
	private final Map<Effect, Node> effects = new HashMap<>();
	private final String channel;
	private static EffectsPane first = null;

	public EffectsPane(final String channel) {
		first = this;
		this.channel = channel;
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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
		final HBox root = new HBox();
		final Button add = new Button("+");
		VBox left = new VBox();
		left.getChildren().add(add);
		left.setAlignment(Pos.CENTER);
		root.getChildren().add(left);
		final ContextMenu menu = new ContextMenu();
		add.setContextMenu(menu);
		effectsType.forEach((e, c) -> {
			MenuItem item = new MenuItem(e);
			menu.getItems().add(item);
		});
		add.setOnMouseClicked(e -> {
			menu.show(this.getScene().getWindow(), e.getScreenX(), e.getScreenY());
		});
		menu.setOnAction(e -> {
			if(((MenuItem)e.getTarget()).getText().equals("Compressor")) {	//Try
				addEffect(new Effect("Compressor"));
			}
		});
		/*root.getChildren().add(new EffectPane(new CompressorPane("Compressor")));
		root.getChildren().add(new EffectPane(new LimiterPane("Limiter")));
		root.getChildren().add(new EffectPane(new PassPane("Pass")));
		root.getChildren().add(new EffectPane(new ReverbPane("Reverb")));*/
		//root.autosize();
		//this.autosize();
		this.setContent(root);
		
	}
	
	private final static Map<String, Class<? extends Pane>> createEffects(){
		Map<String, Class<? extends Pane>> effects = new HashMap<>();
		effects.put("Compressor", CompressorPane.class);
		effects.put("Limiter", LimiterPane.class);
		effects.put("Low pass", PassPane.class);
		effects.put("High pass", PassPane.class);
		effects.put("Reverb", ReverbPane.class);
		return effects;
	}
	
	private final void addEffect(final Effect effect) {
		try {
			effects.put(effect, effectsType.get(effect.getType()).getDeclaredConstructor(String.class).newInstance(effect.getType()));
			this.getChildren().add(new EffectPane(effects.get(effect)));
			if(effect.getType().equals("Compressor")) {
				Starter.getController().addEffectAtPosition(channel, new Compression(1), this.getChildren().indexOf(effects.get(effect)));
			} else if(effect.getType().equals("Limiter")) {
				Starter.getController().addEffectAtPosition(channel, new Limiter(1), this.getChildren().indexOf(effects.get(effect)));
			} else if(effect.getType().equals("Low pass")) {
				Starter.getController().addEffectAtPosition(channel, new LowPassFilter(1), this.getChildren().indexOf(effects.get(effect)));
			} else if(effect.getType().equals("High pass")) {
				Starter.getController().addEffectAtPosition(channel, new HighPassFilter(1), this.getChildren().indexOf(effects.get(effect)));
			} else if(effect.getType().equals("Reverb")) {
				Starter.getController().addEffectAtPosition(channel, new DigitalReverb(1), this.getChildren().indexOf(effects.get(effect)));
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private final void removeEffect(final Effect effect) {
		effects.remove(effect);
		this.getChildren().remove(effects.get(effect));
		Starter.getController().removeEffectAtPosition(channel, this.getChildren().indexOf(effects.get(effect)));
	}
	
	private final void setEffect(final int newPos, Effect effect) {
		this.getChildren().set(newPos, effects.get(effect));
		Starter.getController().moveEffect(channel, newPos, this.getChildren().indexOf(effects.get(effect)));
	}
	
	public final static class EffectPane extends BorderPane {
		
		public EffectPane(final Node effect) {
			final HBox firstrow = new HBox();
			firstrow.setAlignment(Pos.CENTER);
			final Button remove = new Button("X");
			remove.setOnMouseClicked(e -> {
				first.getChildren().remove(this);	//try
			});
			remove.setShape(new Circle(1.5));
			final Button moveLeft = new Button("<");
			moveLeft.setShape(new Circle(1.5));
			final Button moveRight = new Button(">");
			moveRight.setShape(new Circle(1.5));
			HBox.setMargin(remove, new Insets(0,0,0,10));
			
			VBox right = new VBox();
			Button add = new Button("+");
			right.getChildren().add(add);
			right.setAlignment(Pos.CENTER);

			final ContextMenu menu = new ContextMenu();
			add.setContextMenu(menu);
			effectsType.forEach((e, c) -> {
				MenuItem item = new MenuItem(e);
				menu.getItems().add(item);
			});
			add.setOnMouseClicked(e -> {
				menu.show(this.getScene().getWindow(), e.getScreenX(), e.getScreenY());
			});
			
			firstrow.getChildren().addAll(moveLeft, moveRight, remove);
			
			this.setRight(right);
			this.setTop(firstrow);
			this.setCenter(effect);
		}
	}
}
