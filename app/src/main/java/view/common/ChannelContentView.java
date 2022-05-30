package view.common;

import java.util.List;

import Resplan.App;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
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
	
	/**
	 * Draw a new clip currently not displayed.
	 * Add also the new clip to the clip.view set.
	 */
	private void drawClip(Clip clip) {
		Node clipContent = drawClipContent(clip);
		AnchorPane clipView = new AnchorPane(clipContent);
		clipView.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, null)));
		AnchorPane.setBottomAnchor(clipContent, 0.0);
		AnchorPane.setTopAnchor(clipContent, 0.0);
		AnchorPane.setLeftAnchor(clipContent, 0.0);
		AnchorPane.setRightAnchor(clipContent, 0.0);
		clipView.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
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
}
