package view.effects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import daw.core.audioprocessing.Compression;
import daw.core.audioprocessing.DigitalReverb;
import daw.core.audioprocessing.Gate;
import daw.core.audioprocessing.HighPassFilter;
import daw.core.audioprocessing.Limiter;
import daw.core.audioprocessing.LowPassFilter;
import daw.core.audioprocessing.RPEffect;
import resplan.Starter;
import javafx.collections.ObservableList;
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
	
	private final Map<String, Class<? extends Pane>> effectsType = createEffects();
	private final Map<Effect, Node> effects = new HashMap<>();
	private final Map<Class<? extends Pane>, String> paneTypes = createPaneTypes();
	private final Map<Class<? extends RPEffect>, Class<? extends Pane>> translate = createTranslation();
	private final String channel;
	
	private final HBox effectsRoot = new HBox();

	public EffectsPane(final String channel) {
		this.channel = channel;
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		App.getData().getChannel(channel).addFxListListener(c -> {
			c.next();
			if(c.wasAdded()) {
				c.getAddedSubList().forEach(e -> addEffect(e, c.getList().indexOf(e)));
			}

			if(c.wasRemoved()) {
				c.getRemoved().forEach(e -> removeEffect(e));
			}
			
			if(c.wasReplaced()) {
				this.setEffect(c.getList());
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
			Starter.getController().addEffectAtPosition(channel, ((MenuItem) e.getTarget()).getText(), 0);
		});
		root.getChildren().add(effectsRoot);
		Starter.getController().getProcessingUnit(channel).getEffects().forEach(e -> {
			//try {
				var newEffect = new Effect(paneTypes.get(translate.get(e.getClass())));//translate.get(e.getClass()).getDeclaredConstructor(String.class, String.class, int.class).
						//newInstance(paneTypes.get(translate.get(e.getClass())), channel, Starter.getController().getProcessingUnit(channel).getEffects().indexOf(e)); 
				//effects.put(new Effect(paneTypes.get(translate.get(e.getClass()))), newEffect);
				/*effectsRoot.getChildren().add(new EffectPane(newEffect));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {}*/
			addEffect(newEffect, Starter.getController().getProcessingUnit(channel).getEffects().indexOf(e));
		});
		System.out.println(effectsRoot.getChildren().get(0));
		System.out.println(effectsRoot.getChildren().get(1));
		System.out.println(effects);
		this.setContent(root);
	}
	
	private final Map<String, Class<? extends Pane>> createEffects(){
		Map<String, Class<? extends Pane>> effects = new HashMap<>();
		effects.put("Compressor", CompressorPane.class);
		effects.put("Limiter", LimiterPane.class);
		effects.put("Low pass", PassPane.class);
		effects.put("High pass", PassPane.class);
		effects.put("Reverb", ReverbPane.class);
		effects.put("Gate", GatePane.class);
		return effects;
	}
	
	private final Map<Class<? extends Pane>, String> createPaneTypes(){
		Map<Class<? extends Pane>, String> effects = new HashMap<>();
		effects.put(CompressorPane.class, "Compressor");
		effects.put(LimiterPane.class, "Limiter");
		effects.put(PassPane.class, "Low pass");
		effects.put(PassPane.class, "High pass");
		effects.put(ReverbPane.class, "Reverb");
		effects.put(GatePane.class, "Gate");
		return effects;
	}
	
	private final Map<Class<? extends RPEffect>, Class<? extends Pane>> createTranslation(){
		Map<Class<? extends RPEffect>, Class<? extends Pane>> effects = new HashMap<>();
		effects.put(Compression.class, CompressorPane.class);
		effects.put(Limiter.class, LimiterPane.class);
		effects.put(LowPassFilter.class, PassPane.class);
		effects.put(HighPassFilter.class, PassPane.class);
		effects.put(DigitalReverb.class, ReverbPane.class);
		effects.put(Gate.class, GatePane.class);
		return effects;
	}
	
	private final void addEffect(final Effect effect, final int index) {
		try {
			System.out.println(effect + "-" + index);
			final EffectPane newPane = new EffectPane(effectsType.get(effect.getType()).getDeclaredConstructor(String.class, String.class, int.class).
										newInstance(effect.getType(), channel, index));
			effects.put(effect, newPane);
			effectsRoot.getChildren().add(index, newPane);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private final void removeEffect(final Effect effect) {
		effectsRoot.getChildren().remove(effects.get(effect));
		effects.remove(effect);
	}
	
	private final void setEffect(ObservableList<? extends Effect> list) {
		effectsRoot.getChildren().forEach(e -> effectsRoot.getChildren().remove(e));
		list.forEach(e -> {
			try {
				effectsRoot.getChildren().add(new EffectPane(effectsType.get(e.getType()).getDeclaredConstructor(String.class).
						newInstance(e.getType())));
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		});
	}
	
	public final class EffectPane extends BorderPane {
		
		public EffectPane(final Node effect) {
			final HBox firstRow = new HBox();
			firstRow.setAlignment(Pos.CENTER);
			final Button remove = new Button("X");
			remove.setOnMouseClicked(e -> {
				Starter.getController().removeEffectAtPosition(channel, effectsRoot.getChildren().indexOf(this));
			});
			remove.setShape(new Circle(1.5));
			final Button moveLeft = new Button("<");
			moveLeft.setShape(new Circle(1.5));
			moveLeft.setOnMouseClicked(e -> {
				final List<EffectPane> effectPanes = new ArrayList<>();
				effectsRoot.getChildren().forEach(eff -> {
					effectPanes.add((EffectPane) eff);
				});
				final var firstPos = effectsRoot.getChildren().indexOf(this);
				final var secondPos = effectsRoot.getChildren().indexOf(this)-1;
				if(secondPos >= 0) {
					final var firstElement = effectsRoot.getChildren().get(firstPos);
					final var secondElement = effectsRoot.getChildren().get(secondPos);
					effectPanes.set(firstPos, (EffectPane) secondElement);
					effectPanes.set(secondPos, (EffectPane) firstElement);
					
					for(int i = 0; i < effectPanes.size(); i++) {
						Starter.getController().removeEffectAtPosition(channel, 0);
					}
					effectPanes.forEach(ep -> {
						var current = ep.getChildren().get(2).getClass();
						paneTypes.forEach((p, s) -> {
							if(p.equals(current))
								Starter.getController().addEffectAtPosition(channel, s, effectPanes.indexOf(ep));												
						});
					});
				}
			});
			final Button moveRight = new Button(">");
			moveRight.setOnMouseClicked(e -> {
				final List<EffectPane> effectPanes = new ArrayList<>();
				effectsRoot.getChildren().forEach(eff -> {
					effectPanes.add((EffectPane) eff);
				});
				final var firstPos = effectsRoot.getChildren().indexOf(this);
				final var secondPos = effectsRoot.getChildren().indexOf(this)+1;
				if(secondPos < effectPanes.size()) {
					var firstElement = effectsRoot.getChildren().get(firstPos);
					var secondElement = effectsRoot.getChildren().get(secondPos);
					effectPanes.set(firstPos, (EffectPane) secondElement);
					effectPanes.set(secondPos, (EffectPane) firstElement);
					
					for(int i = 0; i < effectPanes.size(); i++) {
						Starter.getController().removeEffectAtPosition(channel, 0);
					}
					effectPanes.forEach(ep -> {
						var current = ep.getChildren().get(2).getClass();
						paneTypes.forEach((p, s) -> {
							if(p.equals(current))
								Starter.getController().addEffectAtPosition(channel, s, effectPanes.indexOf(ep));												
						});
					});
				}
			});
			moveRight.setShape(new Circle(1.5));
			HBox.setMargin(remove, new Insets(0,0,0,10));
			
			final VBox right = new VBox();
			final Button add = new Button("+");
			right.getChildren().add(add);
			right.setAlignment(Pos.CENTER);

			final ContextMenu menu = new ContextMenu();
			add.setContextMenu(menu);
			effectsType.forEach((e, c) -> {
				MenuItem item = new MenuItem(e);
				menu.getItems().add(item);
			});
			add.setOnMouseClicked(e -> menu.show(this.getScene().getWindow(), e.getScreenX(), e.getScreenY()));
			menu.setOnAction(e -> {
				Starter.getController().addEffectAtPosition(channel, ((MenuItem)e.getTarget()).getText(), effectsRoot.getChildren().indexOf(this) + 1);
			});
			
			firstRow.getChildren().addAll(moveLeft, moveRight, remove);
			
			this.setRight(right);
			this.setTop(firstRow);
			this.setCenter(effect);
		}
	}
}
