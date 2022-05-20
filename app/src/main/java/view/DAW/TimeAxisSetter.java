package view.DAW;

import java.util.concurrent.TimeUnit;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

public class TimeAxisSetter {
	
	private final static double MS_TO_MIN = 60000;
	private final static double MS_TO_SEC = 1000;
	private final static double LABELS_PX_GAP = 40;

	private final NumberAxis na = new NumberAxis();
	private final AnchorPane scroller = new AnchorPane();
	
	private double maxTime;
		
	public TimeAxisSetter() {
		na.setAnimated(false);
		na.setAutoRanging(false);
		na.setSide(Side.TOP);
		//set this to Double.MAX_VALUE prevent warnings if the range is changed before a new tick unit is calculated.
		na.setTickUnit(Double.MAX_VALUE);
		calculateRange();
		na.setTickLabelGap(5);
		//------CALCULATE TICK WHEN RESIZE AXIS-------
		na.needsLayoutProperty().addListener((obs, old, needsLayout) -> calculateTicks());
		//-----PRINT TIME INSTEAD OF MILLISECONDS-----
		na.setTickLabelFormatter(new StringConverter<Number>() {

			@Override
			public String toString(Number milliseconds) {
				Long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds.longValue());
				Long sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds.longValue());
				Long ms = milliseconds.longValue()-TimeUnit.SECONDS.toMillis(sec);
				sec -= TimeUnit.MINUTES.toSeconds(min);
				if(na.getTickUnit() < MS_TO_SEC) {
					return String.format("%02d", min) + ":" + String.format("%02d", sec) + "(+" + ms + "ms)";
				}
				return String.format("%02d", min) + ":" + String.format("%02d", sec);
			}

			@Override
			public Number fromString(String string) {
				String[] s = string.split(":");
				if(na.getTickUnit() < MS_TO_SEC) {
					return TimeUnit.SECONDS.toMillis(
								TimeUnit.MINUTES.toSeconds(Long.decode(s[0])) + Long.decode(s[1])) + 
							Long.decode(s[2]);
				}
				return TimeUnit.SECONDS.toMillis(
						TimeUnit.MINUTES.toSeconds(Long.decode(s[0])) +
						Long.decode(s[1]));
			}
			
		});
		//----------SCROLLER-------------
		Rectangle r = new Rectangle();
		r.setWidth(30);
		r.setHeight(scroller.getHeight());
		scroller.getChildren().add(r);
	}

	public NumberAxis getAxis() {
		return na;
	}
	public AnchorPane getScroller() {
		return scroller;
	}
	
	/**
	 * Calculate tick unit for major and minor ticks.
	 * <p>The final unit will be calculated from the actual axis length and it's value range. 
	 * If possible the unit will be also a multiple of standard time units like seconds or minutes.
	 */
	private void calculateTicks() {
		double timeDelta = na.getUpperBound()-na.getLowerBound();
		double axisLength = na.getDisplayPosition(na.getUpperBound())-na.getDisplayPosition(na.getLowerBound());
		Long maxLabels = Double.valueOf(axisLength/LABELS_PX_GAP).longValue();
		double minTickUnit = timeDelta/Math.min(30, maxLabels);
		//round to multiples of min or sec.
		double tick;
		double mTCount;
		if(minTickUnit<=10) {
			tick = 10;
			mTCount = 10;
		} else if(minTickUnit<=100) {
			tick = 100;
			mTCount = 10;
		} else if(minTickUnit<=MS_TO_SEC) {
			tick = MS_TO_SEC;
			mTCount = 10;
		} else if(minTickUnit<=MS_TO_MIN) {
			tick = minTickUnit-(minTickUnit%MS_TO_SEC);
			mTCount = tick/MS_TO_SEC;
		} else {
			tick = minTickUnit-(minTickUnit%MS_TO_MIN);
			mTCount = tick/MS_TO_MIN;
		}
		//set
		na.setTickUnit(Double.valueOf(tick).longValue());
		na.setMinorTickCount(Double.valueOf(mTCount).intValue());
	}
	
	private void calculateRange() {
		na.setLowerBound(0);
		na.setUpperBound(MS_TO_MIN*600);
	}
	
	private void zoom() {
		//TODO
	}

}
