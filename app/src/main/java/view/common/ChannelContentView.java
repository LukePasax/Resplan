package view.common;

import java.util.List;

import Resplan.Starter;
import daw.core.clip.ClipNotFoundException;
import daw.manager.ImportException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public abstract class ChannelContentView extends Pane {

	private final static Paint borderColor = Paint.valueOf("#999999");
	private final NumberAxis axis;
	private final Channel ch;

	public ChannelContentView(Channel ch, NumberAxis axis) {
		super();
		this.ch = ch;
		this.axis = axis;
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
					clip.clearViewSet();
				});
			}
		});
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
		private static final int CLIP_MIN_LENGHT = 10;
		
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
			this.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
			//remove clip
			MenuItem remove = new MenuItem("Remove");
			remove.setOnAction(a->Starter.getController().deleteClip(clip.getTitle(), ch.getTitle(), clip.getPosition()));
			ContextMenu menu = new ContextMenu(remove);
			this.setOnContextMenuRequested(e -> menu.show(this, e.getScreenX(), e.getScreenY()));
			//drag and drop controls
			this.setOnMouseReleased(this::mouseReleased);
			this.setOnMouseMoved(this::mouseOver);
			this.setOnMouseDragged(this::mouseDragged);
			this.setOnMousePressed(this::mousePressed);
		}
		
		private void mouseReleased(MouseEvent e) {
			double newTimeIn = clip.getPosition()+timeDelta;
			if(mod.equals(ClipDragModality.TIMEIN)) {
				//in event
				try {
					if(timeDelta>=clip.getDuration()) {
						newTimeIn = clip.getPosition()+clip.getDuration()-CLIP_MIN_LENGHT;
					}
					if(clip.getPosition()+timeDelta < 0) {
						newTimeIn = 0;
					}
					if(!clip.isEmpty() && clip.getContentPosition()+timeDelta<0) {
						newTimeIn = clip.getPosition()-clip.getContentPosition();
					}
					Starter.getController().moveClip(clip.getTitle(), ch.getTitle(), newTimeIn);
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
						newTimeOut = clip.getPosition()+CLIP_MIN_LENGHT;
					}
					if(!clip.isEmpty() && timeDelta >= clip.getContentDuration()-clip.getContentPosition()-clip.getDuration()) {
						newTimeOut = clip.getPosition()+clip.getContentDuration()-clip.getContentPosition();
					}
					Starter.getController().setClipTimeOut(clip.getTitle(), ch.getTitle(), newTimeOut);
				} catch (ClipNotFoundException | ImportException e1) {
					 updateClips();
				}
			} else {
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
		}
		
		private void mouseOver(MouseEvent e) {
			if(isOnTimeOut(e) || isOnTimeIn(e)) {
				this.setCursor(Cursor.H_RESIZE);
			} else {
				this.setCursor(Cursor.OPEN_HAND);
			}
		}
		
		private void mouseDragged(MouseEvent e) {
			if(dragging) {
				this.timeDelta = axis.getValueForDisplay(e.getScreenX()).doubleValue()-axis.getValueForDisplay(initialX).doubleValue();
				if(mod.equals(ClipDragModality.TIMEIN)) {
					//in event
					if(clip.isEmpty() || clip.getContentPosition()+timeDelta>=0) {
						if(timeDelta<clip.getDuration() && clip.getPosition()+timeDelta >= 0) {
							this.setLayoutX(initialLayoutX+e.getScreenX()-initialX);
							this.setPrefWidth(initialWidth+initialX-e.getScreenX());
						}
					}
				} else if(mod.equals(ClipDragModality.TIMEOUT)) {
					//out event
					if(clip.isEmpty() || timeDelta < clip.getContentDuration()-clip.getContentPosition()-clip.getDuration()) {
						if(clip.getDuration()+timeDelta>0 && clip.getDuration()+clip.getPosition()+timeDelta<=Starter.getController().getProjectLength()) {
							this.setPrefWidth(initialWidth+initialX-e.getScreenX());
						}
					}
				} else {
					//move event
					if(clip.getPosition()+timeDelta >= 0 && clip.getPosition()+clip.getDuration()+timeDelta <= Starter.getController().getProjectLength()) {
						this.setLayoutX(initialLayoutX+e.getScreenX()-initialX);
					}
				}
			}
		}
		
		private void mousePressed(MouseEvent e) {
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
		
		private boolean isOnTimeIn(MouseEvent e) {
			return e.getX()<=RESIZE_MARGIN;	
		}
		
		private boolean isOnTimeOut(MouseEvent e) {
			return e.getX()>= this.getWidth()-RESIZE_MARGIN;
		}
		
	}
}
