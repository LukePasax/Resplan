package view.common;

import java.io.IOException;
import java.util.List;

import Resplan.Starter;
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
import view.planning.NewClipController;

public abstract class ChannelContentView extends Pane {

	private final static Paint borderColor = Paint.valueOf("#999999");
	private final NumberAxis axis;
	private final Channel ch;
	private final ToolBarSetter toolBarSetter;
	private final Color groupColor;
	private final EventHandler<MouseEvent> clickEvent;

	public ChannelContentView(Channel ch, NumberAxis axis, ToolBarSetter toolBarSetter, Color color) {
		super();
		this.ch = ch;
		this.axis = axis;
		this.toolBarSetter = toolBarSetter;
		this.groupColor = color;
		//borderLayout
		this.setBorder(new Border(new BorderStroke(null, null, borderColor, null, 
				null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
		//update clips with layout
		axis.needsLayoutProperty().addListener(x->updateClips());
		//update clips with data add/remove
		App.getData().addClipsDataListener(ch, new ListChangeListener<Clip>() {

			@Override
			public void onChanged(Change<? extends Clip> c) {
				c.next();
				c.getAddedSubList().stream().filter(clip->{
					return clip.getPosition()<axis.getUpperBound() && (clip.getPosition()+clip.getDuration())>axis.getLowerBound();
				}).forEach(clip->{
					drawClip(clip);
				});
				c.getRemoved().forEach(clip->{
					getChildren().removeAll(clip.getViewSet());
				});
			}
		});
		
		//update clips on data changes
		//TODO
		
		//insert empty clip
		class EmptyClipCreator implements EventHandler<MouseEvent> {
			
			private boolean secondClick = false;
			private Double in;
			private Double out;
			
			public EmptyClipCreator() {
				toolBarSetter.addToolChangeListener(()->{
					secondClick = false;
				});
			}

			@Override
			public void handle(MouseEvent e) {
				if(toolBarSetter.getCurrentTool().equals(Tool.ADDCLIPS)) {
					if(secondClick) {
						out = axis.getValueForDisplay(e.getX()).doubleValue();
						if(out<=in) {
							return;
						}
						secondClick = false;
						FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("view/NewClipWindow.fxml"));
			            Scene scene;
						try {
							scene = new Scene(loader.load());
							Stage stage = new Stage();
							NewClipController controller = ((NewClipController)loader.getController());
				            stage.setScene(scene);
				            stage.initModality(Modality.WINDOW_MODAL);
				            stage.setTitle("New Clip");
				            stage.initOwner(getScene().getWindow());
				            controller.channelPicker.setValue(ch.getTitle());
							controller.durationPicker.setText(new NumberFormatConverter().toString(out-in));
							controller.startTimePicker.setText(new NumberFormatConverter().toString(in));
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
		};
		this.clickEvent = new EmptyClipCreator();
	}
	
	public void clickEvent(MouseEvent e) {
		this.clickEvent.handle(e);
	}
	
	//LAYOUT CLIPS
	private double computeChildHeight(Pane child, Double topAnchor, Double bottomAnchor, double areaHeight) {
        final Insets insets = getInsets();
        return areaHeight - insets.getTop() - insets.getBottom() - topAnchor - bottomAnchor;
    }
	
	@Override protected void layoutChildren() {
		final Insets insets = getInsets();
        final List<Pane> children = getManagedChildren();
        for (Pane child : children) {
            final Double topAnchor = 0.0;
            final Double bottomAnchor = 0.0;
            double x = child.getLayoutX();
            double y = child.getLayoutY();
            double h = computeChildHeight(child, topAnchor, bottomAnchor, getHeight());;
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
		if(App.getData().getUnmodifiableChannels().contains(ch)){
			ObservableList<Clip> clips = App.getData().getUnmodifiableClips(ch);
			clips.stream().filter(clipFromData->{
				return clipFromData.getPosition()<axis.getUpperBound() && (clipFromData.getPosition()+clipFromData.getDuration())>axis.getLowerBound();
			}).forEach(clipInTimeRange->{
				if(clipInTimeRange.getViewSet().stream().anyMatch(x->getChildren().contains(x))) {
					clipInTimeRange.getViewSet().stream().filter(x->getChildren().contains(x)).forEach(drawedClip->{
						placeClip((Pane)drawedClip, clipInTimeRange);
					});
				} else {
					drawClip(clipInTimeRange);
				}
			});
			clips.stream().filter(clipFromData->{
				return clipFromData.getPosition()>=axis.getUpperBound() || (clipFromData.getPosition()+clipFromData.getDuration())<=axis.getLowerBound();
			}).forEach(nonInTimeClip->{
				nonInTimeClip.getViewSet().stream().forEach(view->{
					if(getChildren().contains(view)) {
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
	private void drawClip(Clip clip) {
		Node clipContent = drawClipContent(clip);
		AnchorPane clipView = new ClipView(clipContent, clip);
		placeClip(clipView, clip);
		clip.addToViewAll(clipView);
		getChildren().add(clipView);
	}
	
	/**
	 * Place a clip in the correct axis position.
	 */
	private void placeClip(Pane clipView, Clip clip) {
		double inX;
		double outX;
		inX = axis.getDisplayPosition(clip.getPosition());
		outX = axis.getDisplayPosition(clip.getPosition()+clip.getDuration());
		clipView.relocate(inX, 0);
		clipView.setPrefWidth(outX-inX);
	}
	
	public abstract Node drawClipContent(Clip clip);
	
	public Channel getChannel() {
		return this.ch;
	}
	
	/**
	 *	Clip U.I. common code
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
		
		public ClipView(Node content, Clip clip) {
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
			//clip menù
			MenuItem remove = new MenuItem("Remove");
			remove.setOnAction(a->Starter.getController().deleteClip(clip.getTitle(), ch.getTitle(), clip.getPosition()));
			MenuItem loadAudioFile = new MenuItem("Load Audio File");
			loadAudioFile.setOnAction(a->{
				//TODO load file in clip
			});
			MenuItem record = new MenuItem("Record");
			record.setOnAction(a->{
				Starter.getController()/*TODO .recordClip()*/;
				//TODO gestisci clear e record buttons.
			});
			MenuItem clear = new MenuItem("Clear");
			clear.setOnAction(a->{
				Starter.getController()/*TODO .clearClip()*/;
				//TODO gestisci clear e record buttons.
			});
			ContextMenu menu = new ContextMenu(remove, record, loadAudioFile, clear);
			if(clip.isEmpty()) {
				clear.setDisable(true);
			} else {
				record.setDisable(true);
				loadAudioFile.setDisable(true);
			}
			this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
			//drag and drop controls
			this.setOnMouseReleased(this::mouseReleased);
			this.setOnMouseMoved(this::mouseOver);
			this.setOnMouseDragged(this::mouseDragged);
			this.setOnMousePressed(this::mousePressed);
		}
		
		private void mouseReleased(MouseEvent e) {
			if(Starter.getController().isPaused()) {
				double newTimeIn = clip.getPosition()+timeDelta;
				if(mod.equals(ClipDragModality.TIMEIN)) {
					//in event
					try {
						if(timeDelta>=clip.getDuration()) {
							newTimeIn = clip.getPosition()+clip.getDuration()-getMinDuration();
						}
						if(clip.getPosition()+timeDelta < 0) {
							newTimeIn = 0;
						}
						if(!clip.isEmpty() && clip.getContentPosition()+timeDelta<0) {
							newTimeIn = clip.getPosition()-clip.getContentPosition();
						}
						Starter.getController().setClipTimeIn(clip.getTitle(), ch.getTitle(), newTimeIn);
					} catch (ClipNotFoundException | ImportException e1) {
						updateClips();
					}
				} else if(mod.equals(ClipDragModality.TIMEOUT)) {
					//out event
					double newTimeOut = newTimeIn+clip.getDuration();
					try {
						if(newTimeOut>Starter.getController().getProjectLength()) {
							newTimeOut = Starter.getController().getProjectLength();
						}
						if(clip.getDuration()+timeDelta<=0) {
							newTimeOut = clip.getPosition()+getMinDuration();
						}
						if(!clip.isEmpty() && timeDelta >= clip.getContentDuration()-clip.getContentPosition()-clip.getDuration()) {
							newTimeOut = clip.getPosition()+clip.getContentDuration()-clip.getContentPosition();
						}
						Starter.getController().setClipTimeOut(clip.getTitle(), ch.getTitle(), newTimeOut);
					} catch (ClipNotFoundException | ImportException e1) {
						 updateClips();
					}
				} else if (mod.equals(ClipDragModality.MOVE)) {
					//move event
					try {
						if(newTimeIn<0) {
							newTimeIn = 0;
						} else if (newTimeIn+clip.getDuration()>Starter.getController().getProjectLength()) {
							newTimeIn = Starter.getController().getProjectLength()-clip.getDuration();
						}
						Starter.getController().moveClip(clip.getTitle(), ch.getTitle(), newTimeIn);
					} catch (ClipNotFoundException | ImportException e1) {
						updateClips();
					}
				}
				dragging = false;
			}
		}
		
		private void mouseOver(MouseEvent e) {
			if(toolBarSetter.getCurrentTool().equals(Tool.MOVE) && Starter.getController().isPaused()) {
				if(isOnTimeOut(e) || isOnTimeIn(e)) {
					this.setCursor(Cursor.H_RESIZE);
				} else {
					this.setCursor(Cursor.OPEN_HAND);
				}	
			} else {
				this.setCursor(Cursor.DEFAULT);
			}
		}
		
		private void mouseDragged(MouseEvent e) {
			if(dragging) {
				this.timeDelta = axis.getValueForDisplay(e.getScreenX()).doubleValue()-axis.getValueForDisplay(initialX).doubleValue();
				if(mod.equals(ClipDragModality.TIMEIN)) {
					//in event
					if(clip.isEmpty() || clip.getContentPosition()+timeDelta>=0) {
						if(timeDelta<clip.getDuration()-getMinDuration() && clip.getPosition()+timeDelta >= 0) {
							this.setLayoutX(initialLayoutX+e.getScreenX()-initialX);
							this.setPrefWidth(initialWidth+initialX-e.getScreenX());
						}
					}
				} else if(mod.equals(ClipDragModality.TIMEOUT)) {
					//out event
					if(clip.isEmpty() || timeDelta < clip.getContentDuration()-clip.getContentPosition()-clip.getDuration()) {
						if(clip.getDuration()+timeDelta>getMinDuration() && clip.getDuration()+clip.getPosition()+timeDelta<=Starter.getController().getProjectLength()) {
							this.setPrefWidth(initialWidth+e.getScreenX()-initialX);
						}
					}
				} else if (mod.equals(ClipDragModality.MOVE)){
					//move event
					if(clip.getPosition()+timeDelta >= 0 && clip.getPosition()+clip.getDuration()+timeDelta <= Starter.getController().getProjectLength()) {
						this.setLayoutX(initialLayoutX+e.getScreenX()-initialX);
					}
				}
			}
		}
		
		private void mousePressed(MouseEvent e) {
			if(Starter.getController().isPaused()) {
				if(e.getButton().equals(MouseButton.SECONDARY) || !toolBarSetter.getCurrentTool().equals(Tool.MOVE)) {
					this.mod = ClipDragModality.NODRAG;
					if (toolBarSetter.getCurrentTool().equals(Tool.SPLIT)) {
						try {
							Starter.getController().splitClip(clip.getTitle(), ch.getTitle(), axis.getValueForDisplay(axis.getDisplayPosition(clip.getPosition())+e.getX()).doubleValue());
						} catch (ClipNotFoundException | ImportException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					dragging = true;
					initialX = e.getScreenX();
					initialLayoutX = this.getLayoutX();
					initialWidth = this.getWidth();
					if(isOnTimeIn(e)) {
						this.mod = ClipDragModality.TIMEIN;
					} else if(isOnTimeOut(e)) {
						this.mod = ClipDragModality.TIMEOUT;
					} else {
						this.mod = ClipDragModality.MOVE;
					}
				}
			}
		}
		
		private boolean isOnTimeIn(MouseEvent e) {
			return e.getX()<=RESIZE_MARGIN;	
		}
		
		private boolean isOnTimeOut(MouseEvent e) {
			return e.getX()>= this.getWidth()-RESIZE_MARGIN;
		}
		
		private double getMinDuration() {
			return axis.getTickUnit()/10;
		}
		
	}
}
