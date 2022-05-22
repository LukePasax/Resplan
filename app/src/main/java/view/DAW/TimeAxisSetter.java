package view.DAW;

import java.util.concurrent.TimeUnit;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
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
	private double prLength = MS_TO_MIN*10; //project length
	
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
		setRange(0, prLength);
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
				long min = s.length >= 1 ? Long.decode(s[0].replaceFirst("^0*", "").isEmpty() ? "0" : s[0].replaceFirst("^0*", "")) : 0;
				long sec = s.length >= 2 ? Long.decode(s[1].replaceFirst("^0*", "").isEmpty() ? "0" : s[1].replaceFirst("^0*", "")) : 0;
				long ms = s.length >= 3 ? Long.decode(s[2].replaceFirst("^0*", "").isEmpty() ? "0" : s[2].replaceFirst("^0*", "")) : 0;
					return TimeUnit.SECONDS.toMillis(
								TimeUnit.MINUTES.toSeconds(min) + sec) + ms;
			}
			
		});
		//------SET PRECISION NAVIGATION---------
		na.setOnMouseClicked(e->{
			if(e.getButton().equals(MouseButton.SECONDARY)) {
				class AxisMenu extends Popup {
					TextField lb = new TextField();
					TextField ub = new TextField();
					java.util.function.UnaryOperator<Change> f = change -> {
					    String text = change.getText();

					    if (text.matches("[0-9:]*")) {
					        return change;
					    }

					    return null;
					};
					AxisMenu() {
						na.setDisable(true);
						//buttons
						HBox buttons = new HBox();
						Button reset = new Button("RESET");
						reset.setOnAction(a->{
							setRange(0, getProjectLength());
							hide();
						});
						Button ok = new Button("OK");
						ok.setOnAction(a->{
							setBounds();
							hide();
						});
						buttons.getChildren().addAll(reset, ok);
						//lower bound
						HBox lowerBoundSetter = new HBox();	
						lb.setPromptText(na.getTickLabelFormatter().toString(na.getLowerBound()));
						lb.setTextFormatter(new TextFormatter<>(f));
						lowerBoundSetter.getChildren().addAll(new Label("from: "), lb);
						//upper bound
						HBox upperBoundSetter = new HBox();
						ub.setPromptText(na.getTickLabelFormatter().toString(na.getUpperBound()));
						ub.setTextFormatter(new TextFormatter<>(f));
						upperBoundSetter.getChildren().addAll(new Label("to: "), ub);
						//pop-up
						VBox v = new VBox();
						v.setPadding(new Insets(12));
						v.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, null, null)));
						v.setBackground(new Background(new BackgroundFill(Paint.valueOf("#999999"), null, null)));
						v.getChildren().addAll(new Label("Set axis range"), lowerBoundSetter, upperBoundSetter, buttons);
						this.getContent().add(v);
						this.setOnHidden(e->na.setDisable(false));
					}
					void setBounds() {
						if(lb.getText().isBlank() || ub.getText().isBlank()) {
							return;
						}
						StringConverter<Number> sc = na.getTickLabelFormatter();
						setRange(sc.fromString(lb.getText()).doubleValue(), sc.fromString(ub.getText()).doubleValue());	
					}
				}
				Popup p = new AxisMenu();
				p.show(na.getScene().getWindow(), e.getScreenX(), e.getScreenY());
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
		//------DRAG RESIZE AND NAVIGATE-----------
		TimeAxisDragNavigator.makeNavigable(this);
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
	public void setRange(double lowerBound, double upperBound) {
		if(lowerBound<0 || upperBound>prLength || lowerBound>=upperBound) {
			throw new IllegalArgumentException("Lower and upper bounds must be between 0 and project length.");
		}
		na.setLowerBound((lowerBound));
		na.setUpperBound((upperBound));
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
	 * Return a number between 0 and 1. ex: 0.2 = 20%;
	 * @param total
	 * @param part
	 * @return
	 */
	private double calculatePercent(double total, double part) {
		return part/total;
	}
	
	//other getters
	public double getProjectLength() {
		return prLength;
	}
}

