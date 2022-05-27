package view.common;

import Resplan.App;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import view.common.ViewDataImpl.Marker;

public class MarkersPane extends Pane {
	
	private final NumberAxis axis;
	 private final Path timeMarkers = new Path();

	public MarkersPane(NumberAxis axis) {
		this.setMouseTransparent(true);
		timeMarkers.setStroke(Color.DARKGREY);
		this.getChildren().add(timeMarkers);
		this.axis = axis;
		axis.needsLayoutProperty().addListener(x->{updatePositions(); updateTimeMarkers();});
	}

	private void updatePositions() {
		ObservableSet<Marker> markers = App.getData().getUnmodifiableMarkers();
		markers.stream().filter(marker->{
			return marker.getPosition()<axis.getUpperBound() && marker.getPosition()>axis.getLowerBound();
		}).forEach(markerInTimeRange->{
			if(markerInTimeRange.getViewSet().stream().anyMatch(x->getChildren().contains(x))) {
				markerInTimeRange.getViewSet().stream().filter(x->getChildren().contains(x)).forEach(drawedMarker->{
					placeMarker((Line)drawedMarker, markerInTimeRange);
				});
			} else {
				drawClip(markerInTimeRange);
			}
		});
	}

	private void drawClip(Marker markerInTimeRange) {
		
	}

	private void placeMarker(Line markerView, Marker markerInTimeRange) {
		//markerView.relocate(markerInTimeRange.getPosition(), 0);
	}
	
	private void updateTimeMarkers() {
		final ObservableList<Axis.TickMark<Number>> tickMarks = axis.getTickMarks();
		timeMarkers.getElements().clear();
        for(int i=0; i < tickMarks.size(); i++) {
            Axis.TickMark<Number> tick = tickMarks.get(i);
            final double x = axis.getDisplayPosition(tick.getValue());
            if ((x != axis.getZeroPosition()) && x > 0 && x <= axis.getWidth()) {
                timeMarkers.getElements().add(new MoveTo(x+0.5,axis.getLayoutY()+axis.getHeight()));
                timeMarkers.getElements().add(new LineTo(x+0.5, this.getHeight()));
            }
        } 
	}
	
}
