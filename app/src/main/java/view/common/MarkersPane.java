package view.common;

import Resplan.Starter;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import view.common.ViewDataImpl.Section;

public class MarkersPane extends Pane {
	
	private final NumberAxis axis;
	private final Path timeMarkers = new Path();
	private final Path sectionMarkers = new Path();
	private final Path playbackMarker = new Path();
	private final Pane labels = new Pane();

	public MarkersPane(final NumberAxis axis) {
		this.setMouseTransparent(true);
		timeMarkers.setStroke(Color.DARKGREY);
		sectionMarkers.setStroke(Color.ORANGE);
		playbackMarker.setStroke(Color.BLACK);
		labels.setMaxHeight(axis.getLayoutY());
		this.getChildren().addAll(timeMarkers, sectionMarkers, labels, playbackMarker);

		this.axis = axis;
		//update when axis layout
		axis.needsLayoutProperty().addListener(x -> updateAll());
		//update on data changes
		App.getData().addSectionDataListener(x -> updateSectionMarkers());
		App.getData().getProjectLenghtProperty().addListener(x -> updateAll());
		//vertical layout resize
		this.heightProperty().addListener(x -> updateAll());
	}
	
	private void updateAll() {
		updateSectionMarkers();
		updateTimeMarkers();
		updatePlaybackMarker(Starter.getController().getPlaybackTime());
	}

	//-------SECTIONS-------
	private void updateSectionMarkers() {
		sectionMarkers.getElements().clear();
		labels.getChildren().clear();
		ObservableSet<Section> markers = App.getData().getUnmodifiableSections();
		markers.stream().filter(marker -> {
			return marker.getPosition() < axis.getUpperBound() && marker.getPosition() > axis.getLowerBound();
		}).forEach(markerInTimeRange -> {
            final double x = axis.getDisplayPosition(markerInTimeRange.getPosition());
            if ((x != axis.getZeroPosition()) && x > 0 && x <= axis.getWidth()) {
            	//marker (path)
                sectionMarkers.getElements().add(new MoveTo(x + 0.5, axis.getLayoutY() + axis.getHeight()));
                sectionMarkers.getElements().add(new LineTo(x + 0.5, this.getHeight()));
                //marker title (label + path)
                sectionMarkers.getElements().add(new MoveTo(x + 0.5, 0));
                sectionMarkers.getElements().add(new LineTo(x + 0.5, axis.getLayoutY() - 10));
                Label title = new Label(markerInTimeRange.getTitle());
                title.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
                title.setMaxHeight(axis.getLayoutY());
                title.setMinWidth(title.getPrefWidth());
                title.relocate(x + 2, 0);
                labels.getChildren().add(title);
            }
		});
	}
	
	//-----TIME MARKERS------
	private void updateTimeMarkers() {
		final ObservableList<Axis.TickMark<Number>> tickMarks = axis.getTickMarks();
		timeMarkers.getElements().clear();
        for (int i = 0; i < tickMarks.size(); i++) {
            Axis.TickMark<Number> tick = tickMarks.get(i);
            final double x = axis.getDisplayPosition(tick.getValue());
            if ((x != axis.getZeroPosition()) && x > 0 && x <= axis.getWidth()) {
                timeMarkers.getElements().add(new MoveTo(x + 0.5, axis.getLayoutY() + axis.getHeight()));
                timeMarkers.getElements().add(new LineTo(x + 0.5, this.getHeight()));
            }
        } 
	}
	
	//-----PLAYBACK MARKER------
	/**
	 * Update the playback marker position.
	 * 
	 * @param  time  The time where to display the playback marker.
	 */
	public void updatePlaybackMarker(final double time) {
		playbackMarker.getElements().clear();
		double x = axis.getDisplayPosition(time);
		if ((x != axis.getZeroPosition()) && x > 0 && x <= axis.getWidth()) {
			playbackMarker.getElements().add(new MoveTo(x + 0.5, 0));
			playbackMarker.getElements().add(new LineTo(x + 0.5, this.getHeight()));
		}
	}
}
