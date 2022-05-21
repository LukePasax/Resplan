package view.DAW;

import java.util.concurrent.TimeUnit;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

public class TimeAxisSetter {
	
	private final static double MS_TO_MIN = 60000;
	private final static double MS_TO_SEC = 1000;
	private final static double LABELS_PX_GAP = 40;

	private final NumberAxis na = new NumberAxis();
	private final AnchorPane scrollerPane = new AnchorPane();
	private final Rectangle scroller = new Rectangle();

	//TODO set and update prLenght with model!!!
	/**
	 * The time length of the project.
	 */
	private double prLength = MS_TO_MIN*60; //project length
	
	/**
	 * The time interval displayed on axis.
	 */
	private double timeDelta;
		
	public TimeAxisSetter() {
	//---------NUMBER AXIS-------------
		na.setAnimated(false);
		na.setAutoRanging(false);
		na.setSide(Side.TOP);
		//set this to Double.MAX_VALUE prevent warnings if the range is changed before a new tick unit is calculated.
		na.setTickUnit(Double.MAX_VALUE);
		//set the range to 100% prLength
		setRange(MS_TO_MIN*30, MS_TO_MIN*40);
		na.setTickLabelGap(5);
		//------CALCULATE TICK WHEN RESIZE AXIS-------
		na.needsLayoutProperty().addListener((obs, old, needsLayout) -> {calculateTicks(); updateScroller();});
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
	//----------SCROLLER----------------
		scroller.setFill(Paint.valueOf("#444444"));
		AnchorPane.setBottomAnchor(scroller, 0.0);
		AnchorPane.setTopAnchor(scroller, 0.0);
		scroller.setHeight(5);
		scroller.setArcHeight(5);
		scroller.setArcWidth(5);
		scrollerPane.getChildren().add(scroller);
		//------SCROLLER DRAG RESIZE-----------
		
	}

	//---------FX COMPONENTS GETTERS---------------
	public NumberAxis getAxis() {
		return na;
	}
	public AnchorPane getScroller() {
		return scrollerPane;
	}
	
	//---------CALCULATE AXIS---------------------
	/**
	 * Calculate tick unit for major and minor ticks.
	 * <p>The final unit will be calculated from the actual axis length and it's value range. 
	 * If possible the unit will be also a multiple of standard time units like seconds or minutes.
	 * Needs to be called every time number axis needs layout;
	 */
	private void calculateTicks() {
		Long maxLabels = Double.valueOf(na.getWidth()/LABELS_PX_GAP).longValue();
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
	
	/**
	 * Set a new range for the timeline. Responsable of update scroller position.
	 * @param lowerBound
	 * @param upperBound
	 */
	private void setRange(double lowerBound, double upperBound) {
		if(lowerBound<0 || upperBound>prLength || lowerBound>=upperBound) {
			throw new IllegalArgumentException("Lower and upper bounds must be between 0 and project length.");
		}
		na.setLowerBound(lowerBound);
		na.setUpperBound(upperBound);
		timeDelta = na.getUpperBound()-na.getLowerBound();
		updateScroller();
	}
	
	//---------CALCULATE SCROLLER-----------------
	/**
	 * Update scroller dimension and position.
	 * Needs to be called every time:<ul>
	 * <li> the number axis needs layout
	 * <li> the prLength change
	 * <li> axis bounds are changed (axis zoom)
	 * </ul>
	 */
	private void updateScroller() {
		//length
		scroller.setWidth(na.getWidth()*calculatePercent(prLength, timeDelta));
		//position
		double pos = na.getWidth()*calculatePercent(prLength, na.getLowerBound());
		AnchorPane.setLeftAnchor(scroller, pos);
	}
	
	/**
	 * Return a number between 0 and 1. es: 0.2 = 20%;
	 * @param total
	 * @param part
	 * @return
	 */
	private double calculatePercent(double total, double part) {
		return part/total;
	}
}

