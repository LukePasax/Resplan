package view.effects;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import resplan.Starter;
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
	
	private final static HBox effectsRoot = new HBox();

	public EffectsPane(final String channel) {
		this.channel = channel;
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		App.getData().getChannel(channel).addFxListListener(c -> {
			c.next();
			if(c.wasAdded()) {
				c.getAddedSubList().forEach(e -> addEffect(new Effect(e.getType())));
			}

			if(c.wasRemoved()) {
				c.getAddedSubList().forEach(this::removeEffect);
			}

			if(c.wasPermutated()) {
				c.getAddedSubList().forEach(e -> setEffect(c.getPermutation(c.getList().indexOf(e)), e));
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
		add.setOnMouseClicked(e -> menu.show(this.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
		menu.setOnAction(e -> {
			Starter.getController().addEffectAtPosition(channel, ((MenuItem)e.getTarget()).getText(), 0);
		});
		root.getChildren().add(effectsRoot);
		this.setContent(root);
		
	}
	
	private static Map<String, Class<? extends Pane>> createEffects(){
		Map<String, Class<? extends Pane>> effects = new HashMap<>();
		effects.put("Compressor", CompressorPane.class);
		effects.put("Limiter", LimiterPane.class);
		effects.put("Low pass", PassPane.class);
		effects.put("High pass", PassPane.class);
		effects.put("Reverb", ReverbPane.class);
		return effects;
	}
	
	private void addEffect(final Effect effect) {
		try {
			final EffectPane newPane = new EffectPane(effectsType.get(effect.getType()).getDeclaredConstructor(String.class).
										newInstance(effect.getType()));
			effects.put(effect, newPane);
			effectsRoot.getChildren().add(newPane);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private void removeEffect(final Effect effect) {
		this.getChildren().remove(effects.get(effect));
		Starter.getController().removeEffectAtPosition(channel, this.getChildren().indexOf(effects.get(effect)));
		effects.remove(effect);
	}
	
	private void setEffect(final int newPos, Effect effect) {
		this.getChildren().set(newPos, effects.get(effect));
	}
	
	public final class EffectPane extends BorderPane {
		
		public EffectPane(final Node effect) {
			final HBox firstRow = new HBox();
			firstRow.setAlignment(Pos.CENTER);
			final Button remove = new Button("X");
			remove.setOnMouseClicked(e -> {
				effectsRoot.getChildren().remove(this);	//try
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
			add.setOnMouseClicked(e -> menu.show(this.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
			
			firstRow.getChildren().addAll(moveLeft, moveRight, remove);
			
			this.setRight(right);
			this.setTop(firstRow);
			this.setCenter(effect);
		}
	}
}
