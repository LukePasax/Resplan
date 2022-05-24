package view.daw;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ChannelClipsPane extends AnchorPane {
	
	private final static Paint borderColor = Paint.valueOf("#999999");
	private final NumberAxis axis;
	private final ObservableList<Clip> clips = FXCollections.observableArrayList();

	public ChannelClipsPane(NumberAxis axis) {
		super();
		this.axis = axis;
		//borderLayout
		this.setBorder(new Border(new BorderStroke(null, null, borderColor, null, 
				null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, null, null)));
		clips.addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable observable) {
				updateClips();
			}
			
		});
		updateClips();
	}
	
	public void updateClips() {
		this.clips.forEach(clip->{
			clip.relocate(axis.getDisplayPosition(clip.getIn()), 20);
			clip.setPrefWidth(axis.getDisplayPosition(clip.getOut())-axis.getDisplayPosition(clip.getIn()));
			this.getChildren().add(clip);
		});
		
		//clip pane
		Pane l = new Pane();
		l.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
		l.setMinWidth(40);
		AnchorPane.setBottomAnchor(l, 0.0);
		AnchorPane.setTopAnchor(l, 0.0);
		l.relocate(axis.getDisplayPosition(60000), 0);
		this.getChildren().add(l);
	}
	
	public ObservableList<Clip> getClips(){
		return this.clips;
	}
	
	public static class Clip extends Pane {
		
		private double in;
		
		private double out;
		
		public Clip() {
			this.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
		}

		public double getIn() {
			return in;
		}

		public void setIn(double in) {
			this.in = in;
		}

		public double getOut() {
			return out;
		}

		public void setOut(double out) {
			this.out = out;
		}
		
	}
}
