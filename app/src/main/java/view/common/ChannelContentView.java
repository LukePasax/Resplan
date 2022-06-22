package view.common;

import java.io.File;
import java.io.IOException;
import java.util.List;
import resplan.Starter;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;
import view.planning.ClipDescriptionEditorController;
import view.planning.ClipTextEditorController;
import view.planning.NewClipController;

/**
 * The view for a channel content. 
 * This Pane must display all the clips of a channel in the correct position.
 * Extends this class and override {@code drawClipContent(Clip clip)} for 
 * personalize the clip view.
 */
public abstract class ChannelContentView extends Pane {

	/**
	 * Color of the border of this channel view.
	 */
	private static final Paint BORDER_COLOR = Paint.valueOf("#999999");
	
	/**
	 * Axis for place clips in the correct position.
	 */
	private final NumberAxis axis;
	
	/**
	 * The channel displayed by this channel view.
	 */
	private final Channel ch;
	
	/**
	 * The tool bar setter, for get the current selected tool.
	 */
	private final ToolBarSetter toolBarSetter;
	
	/**
	 * The color of the group of this channel.
	 */
	private final Color groupColor;
	
	/**
	 * Handler for the click event (for empty clip creation).
	 */
	private final EventHandler<MouseEvent> clickEventHandler;

	/**
	 * Creates a new channel content view.
	 * 
	 * @param ch the channel to display.
	 * 
	 * @param axis the axis for correctly place clips.
	 * 
	 * @param toolBarSetter the tool bar setter.
	 * 
	 * @param color the color of the channel group.
	 */
	public ChannelContentView(final Channel ch, final NumberAxis axis, final ToolBarSetter toolBarSetter, final Color color) {
		super();
		this.ch = ch;
		this.axis = axis;
		this.toolBarSetter = toolBarSetter;
		this.groupColor = color;
		//borderLayout
		this.setBorder(new Border(new BorderStroke(null, null, BORDER_COLOR, null, 
				null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
		//update clips with layout
		axis.needsLayoutProperty().addListener(x -> updateClips());
		//update clips with data add/remove
		App.getData().addClipsDataListener(ch, new ListChangeListener<Clip>() {

			@Override
			public void onChanged(final Change<? extends Clip> c) {
				c.next();
				c.getAddedSubList().stream().filter(clip -> {
					return clip.getPosition().get() < axis.getUpperBound() && (clip.getPosition().get() + clip.getDuration().get()) > axis.getLowerBound();
				}).forEach(clip -> {
					drawClip(clip);
				});
				c.getRemoved().forEach(clip -> {
					getChildren().removeAll(clip.getViewSet());
				});
			}
		});

		//insert empty clip
		class EmptyClipCreator implements EventHandler<MouseEvent> {

			private boolean secondClick = false;
			private Double in;
			private Double out;

			EmptyClipCreator() {
				toolBarSetter.addToolChangeListener(() -> {
					secondClick = false;
				});
			}

			@Override
			public void handle(final MouseEvent e) {
				if (toolBarSetter.getCurrentTool().equals(Tool.ADDCLIPS)) {
					if (secondClick) {
						out = axis.getValueForDisplay(e.getX()).doubleValue();
						if (out <= in) {
							return;
						}
						secondClick = false;
						FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/NewClipWindow.fxml"));
			            Scene scene;
						try {
							scene = new Scene(loader.load());
							Stage stage = new Stage();
							NewClipController controller = ((NewClipController) loader.getController());
				            stage.setScene(scene);
				            stage.initModality(Modality.WINDOW_MODAL);
				            stage.setTitle("New Clip");
				            stage.initOwner(getScene().getWindow());
				            controller.getChannelPicker().setValue(ch.getTitle());
							controller.getDurationPicker().setText(new NumberFormatConverter().toString(out - in));
							controller.getStartTimePicker().setText(new NumberFormatConverter().toString(in));
				            stage.showAndWait();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						in = axis.getValueForDisplay(e.getX()).doubleValue();
						secondClick = true;
					}
				}
			}
		}
		this.clickEventHandler = new EmptyClipCreator();
	}
	
	/**
	 * Handle a click event.
	 * 
	 * @param  e  The event to handle.
	 */
	public void clickEvent(final MouseEvent e) {
		this.clickEventHandler.handle(e);
	}
	
	//LAYOUT CLIPS
	private double computeChildHeight(final Pane child, final Double topAnchor, final Double bottomAnchor, final double areaHeight) {
        final Insets insets = getInsets();
        return areaHeight - insets.getTop() - insets.getBottom() - topAnchor - bottomAnchor;
    }
	
	/**
	 *	Layout the childrens of this pane.
	 */
	@Override 
	protected void layoutChildren() {
		final Insets insets = getInsets();
        final List<Pane> children = getManagedChildren();
        for (Pane child : children) {
            final Double topAnchor = 0.0;
            final Double bottomAnchor = 0.0;
            double x = child.getLayoutX();
            double y = child.getLayoutY();
            double h = computeChildHeight(child, topAnchor, bottomAnchor, getHeight());
            double w = child.getPrefWidth();
            y = insets.getTop() + topAnchor;
            y = getHeight() - insets.getBottom() - bottomAnchor - h;
            child.resizeRelocate(x, y, w, h);
        }
	}
	
	//UPDATE AND DRAW CLIPS
	/**
	 * Does nothing if every clip contained in data is drawed and in it's size is right.
	 * Else update the view in order to correctly display the clips.
	 * The clip.view set will be updated with currently displayed clips.
	 */
	public void updateClips() {
		if (App.getData().getUnmodifiableChannels().contains(ch)) {
			ObservableList<Clip> clips = App.getData().getUnmodifiableClips(ch);
			clips.stream().filter(clipFromData -> {
				return clipFromData.getPosition().get() < axis.getUpperBound() && (clipFromData.getPosition().get() + clipFromData.getDuration().get()) > axis.getLowerBound();
			}).forEach(clipInTimeRange -> {
				if (clipInTimeRange.getViewSet().stream().anyMatch(x -> getChildren().contains(x))) {
					clipInTimeRange.getViewSet().stream().filter(x -> getChildren().contains(x)).forEach(drawedClip -> {
						placeClip((Pane) drawedClip, clipInTimeRange);
					});
				} else {
					drawClip(clipInTimeRange);
				}
			});
			clips.stream().filter(clipFromData -> {
				return clipFromData.getPosition().get() >= axis.getUpperBound() || (clipFromData.getPosition().get() + clipFromData.getDuration().get()) <= axis.getLowerBound();
			}).forEach(nonInTimeClip -> {
				nonInTimeClip.getViewSet().stream().forEach(view -> {
					if (getChildren().contains(view)) {
						getChildren().remove(view);
					}
				});
			});
		}
	}
	
	/**
	 * Draw a new clip currently not displayed.
	 * Add also the new clip to the clip.view set.
	 */
	private void drawClip(final Clip clip) {
		Node clipContent = drawClipContent(clip);
		AnchorPane clipView = new ClipView(clipContent, clip);
		placeClip(clipView, clip);
		clip.getDuration().addListener(o -> {
			placeClip(clipView, clip);
		});
		clip.getPosition().addListener(o -> {
			placeClip(clipView, clip);
		});
		clip.addToViewAll(clipView);
		getChildren().add(clipView);
	}
	
	/**
	 * Place a clip in the correct axis position.
	 */
	private void placeClip(final Pane clipView, final Clip clip) {
		double inX;
		double outX;
		inX = axis.getDisplayPosition(clip.getPosition().get());
		outX = axis.getDisplayPosition(clip.getPosition().get() + clip.getDuration().get());
		clipView.relocate(inX, 0);
		clipView.setPrefWidth(outX - inX);
	}
	
	public abstract Node drawClipContent(Clip clip);
	
	/**
	 * Get the channel displayed by this channel content view.
	 * 
	 * @return the channel displayed by this channel content view.
	 */
	public Channel getChannel() {
		return this.ch;
	}
	
	/**
	 *	Clip view pane.
	 */
	private class ClipView extends AnchorPane {

		private static final int RESIZE_MARGIN = 5;
		private double initialX;
		private double initialLayoutX;
		private double initialWidth;
		private double timeDelta;
		private ClipDragModality mod;
		private boolean dragging = false;
		private Clip clip;

		ClipView(final Node content, final Clip clip) {
			super(content);
			this.clip = clip;
			//clip border
			this.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, null)));
			//content layout
			AnchorPane.setBottomAnchor(content, 0.0);
			AnchorPane.setTopAnchor(content, 0.0);
			AnchorPane.setLeftAnchor(content, 0.0);
			AnchorPane.setRightAnchor(content, 0.0);
			//clip color fill
			this.setBackground(new Background(new BackgroundFill(groupColor, null, null)));
			Pane overlay = new Pane();
			AnchorPane.setBottomAnchor(overlay, 0.0);
			AnchorPane.setTopAnchor(overlay, 0.0);
			AnchorPane.setLeftAnchor(overlay, 0.0);
			AnchorPane.setRightAnchor(overlay, 0.0);
			overlay.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FFFFFF"), null, null)));
			overlay.setBlendMode(BlendMode.HARD_LIGHT);
			overlay.setOpacity(0.5);
			overlay.setMouseTransparent(true);
			this.getChildren().add(0, overlay);
			// clip menu
			MenuItem remove = new MenuItem("Remove");
			remove.setOnAction(a -> Starter.getController().deleteClip(clip.getTitle(), ch.getTitle(), clip.getPosition().get()));
			MenuItem loadAudioFile = new MenuItem("Load Audio File");
			loadAudioFile.setOnAction(a -> {
				WavFilePicker picker = new WavFilePicker();
		        File file = picker.getFileChooser().showOpenDialog(this.getScene().getWindow());
		        if (file != null) {
		            try {
						Starter.getController().addContentToClip(clip.getTitle(), file);
					} catch (ImportException | ClipNotFoundException e1) {
						e1.printStackTrace();
					}
		        }
			});
			MenuItem record = new MenuItem("Record");
			record.setOnAction(a -> {
				FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/RecorderView.fxml"));
		        Scene scene;
				try {
					scene = new Scene(loader.load());
					Stage stage = new Stage();
			        stage.setScene(scene);
			        stage.initModality(Modality.WINDOW_MODAL);
			        stage.setTitle("Recorder");
			        stage.initOwner(this.getScene().getWindow());
			        stage.setResizable(false);
			        ((RecorderController) loader.getController()).setClipTitle(clip.getTitle());
			        stage.showAndWait();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
			MenuItem clear = new MenuItem("Clear content");
			clear.setOnAction(a -> {
				try {
					Starter.getController().removeContentFromClip(clip.getTitle());
				} catch (ClipNotFoundException e) {
					e.printStackTrace();
				}
			});
			MenuItem clipText = new MenuItem("Edit text");
			clipText.setOnAction(event -> {
				try {
					FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/TextEditorView.fxml"));
					loader.setController(new ClipTextEditorController());
					Stage stage = new Stage();
					stage.setScene(new Scene(loader.load()));
					stage.setTitle("Text Editor - " + clip.getTitle() + " text");
					stage.initOwner(this.getScene().getWindow());
					((ClipTextEditorController) loader.getController()).setTitle(clip.getTitle());
					stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			MenuItem clipDescription = new MenuItem("Edit description");
			clipDescription.setOnAction(event -> {
				try {
					FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/TextEditorView.fxml"));
					loader.setController(new ClipDescriptionEditorController());
					Stage stage = new Stage();
					stage.setScene(new Scene(loader.load()));
					stage.setTitle("Text Editor - " + clip.getTitle() + " description");
					stage.initOwner(this.getScene().getWindow());
					((ClipDescriptionEditorController) loader.getController()).setTitle(clip.getTitle());
					stage.showAndWait();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			ContextMenu menu = new ContextMenu(remove, record, loadAudioFile, clear, clipText, clipDescription);
			if (clip.isEmpty()) {
				clear.setDisable(true);
			} else {
				record.setDisable(true);
				loadAudioFile.setDisable(true);
			}
			if (!Starter.getController().getClipType(clip.getTitle()).equals("SPEECH")) {
				clipText.setDisable(true);
			}
			this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
			//drag and drop controls
			this.setOnMouseReleased(this::mouseReleased);
			this.setOnMouseMoved(this::mouseOver);
			this.setOnMouseDragged(this::mouseDragged);
			this.setOnMousePressed(this::mousePressed);
		}

		private void mouseReleased(final MouseEvent e) {
			if (Starter.getController().isPaused()) {
				double newTimeIn = clip.getPosition().get() + timeDelta;
				if (mod.equals(ClipDragModality.TIMEIN)) {
					//in event
					try {
						if (timeDelta >= clip.getDuration().get()) {
							newTimeIn = clip.getPosition().get() + clip.getDuration().get() - getMinDuration();
						}
						if (clip.getPosition().get() + timeDelta < 0) {
							newTimeIn = 0;
						}
						if (!clip.isEmpty() && clip.getContentPosition() + timeDelta < 0) {
							newTimeIn = clip.getPosition().get() - clip.getContentPosition();
						}
						Starter.getController().setClipTimeIn(clip.getTitle(), ch.getTitle(), newTimeIn);
					} catch (ClipNotFoundException e1) {
						updateClips();
					}
				} else if (mod.equals(ClipDragModality.TIMEOUT)) {
					//out event
					double newTimeOut = newTimeIn + clip.getDuration().get();
					try {
						if (newTimeOut > Starter.getController().getProjectLength()) {
							newTimeOut = Starter.getController().getProjectLength();
						}
						if (clip.getDuration().get() + timeDelta <= 0) {
							newTimeOut = clip.getPosition().get() + getMinDuration();
						}
						if (!clip.isEmpty() && timeDelta >= clip.getContentDuration() - clip.getContentPosition() - clip.getDuration().get()) {
							newTimeOut = clip.getPosition().get() + clip.getContentDuration() - clip.getContentPosition();
						}
						Starter.getController().setClipTimeOut(clip.getTitle(), ch.getTitle(), newTimeOut);
					} catch (ClipNotFoundException e1) {
						 updateClips();
					}
				} else if (mod.equals(ClipDragModality.MOVE)) {
					//move event
					try {
						if (newTimeIn < 0) {
							newTimeIn = 0;
						} else if (newTimeIn + clip.getDuration().get() > Starter.getController().getProjectLength()) {
							newTimeIn = Starter.getController().getProjectLength() - clip.getDuration().get();
						}
						Starter.getController().moveClip(clip.getTitle(), ch.getTitle(), newTimeIn);
					} catch (ClipNotFoundException e1) {
						updateClips();
					}
				}
				dragging = false;
				App.getData().getUnmodifiableClips(ch).forEach(c -> c.getViewSet().forEach(n -> n.setOpacity(1)));
			}
		}

		private void mouseOver(final MouseEvent e) {
			if (toolBarSetter.getCurrentTool().equals(Tool.MOVE) && Starter.getController().isPaused()) {
				if (isOnTimeOut(e) || isOnTimeIn(e)) {
					this.setCursor(Cursor.H_RESIZE);
				} else {
					this.setCursor(Cursor.OPEN_HAND);
				}	
			} else {
				this.setCursor(Cursor.DEFAULT);
			}
		}

		private void mouseDragged(final MouseEvent e) {
			if (dragging) {
				this.timeDelta = axis.getValueForDisplay(e.getScreenX()).doubleValue() - axis.getValueForDisplay(initialX).doubleValue();
				if (mod.equals(ClipDragModality.TIMEIN)) {
					//in event
					if (clip.isEmpty() || clip.getContentPosition() + timeDelta >= 0) {
						if (timeDelta < clip.getDuration().get() - getMinDuration() && clip.getPosition().get() + timeDelta >= 0) {
							this.setLayoutX(initialLayoutX + e.getScreenX() - initialX);
							this.setPrefWidth(initialWidth + initialX - e.getScreenX());
						}
					}
				} else if (mod.equals(ClipDragModality.TIMEOUT)) {
					//out event
					if (clip.isEmpty() || timeDelta < clip.getContentDuration() - clip.getContentPosition() - clip.getDuration().get()) {
						if (clip.getDuration().get() + timeDelta > getMinDuration() && clip.getDuration().get() + clip.getPosition().get() + timeDelta <= Starter.getController().getProjectLength()) {
							this.setPrefWidth(initialWidth + e.getScreenX() - initialX);
						}
					}
				} else if (mod.equals(ClipDragModality.MOVE)) {
					//move event
					if (clip.getPosition().get() + timeDelta >= 0 && clip.getPosition().get() + clip.getDuration().get() + timeDelta <= Starter.getController().getProjectLength()) {
						this.setLayoutX(initialLayoutX + e.getScreenX() - initialX);
					}
				}
			}
		}

		private void mousePressed(final MouseEvent e) {
			if (Starter.getController().isPaused()) {
				if (e.getButton().equals(MouseButton.SECONDARY) || !toolBarSetter.getCurrentTool().equals(Tool.MOVE)) {
					this.mod = ClipDragModality.NODRAG;
					if (toolBarSetter.getCurrentTool().equals(Tool.SPLIT)) {
						try {
							Starter.getController().splitClip(clip.getTitle(), ch.getTitle(), axis.getValueForDisplay(axis.getDisplayPosition(clip.getPosition().get()) + e.getX()).doubleValue());
						} catch (ClipNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					App.getData().getUnmodifiableClips(ch).forEach(c -> c.getViewSet().forEach(n -> n.setOpacity(0.5)));
					dragging = true;
					initialX = e.getScreenX();
					initialLayoutX = this.getLayoutX();
					initialWidth = this.getWidth();
					if (isOnTimeIn(e)) {
						this.mod = ClipDragModality.TIMEIN;
					} else if (isOnTimeOut(e)) {
						this.mod = ClipDragModality.TIMEOUT;
					} else {
						this.mod = ClipDragModality.MOVE;
					}
				}
			}
		}

		private boolean isOnTimeIn(final MouseEvent e) {
			return e.getX() <= RESIZE_MARGIN;	
		}

		private boolean isOnTimeOut(final MouseEvent e) {
			return e.getX() >= this.getWidth() - RESIZE_MARGIN;
		}

		private double getMinDuration() {
			return axis.getTickUnit() / 10;
		}
	}
}
