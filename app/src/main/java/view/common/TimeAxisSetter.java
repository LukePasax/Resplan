package view.common;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Setter for time axis.
 */
public final class TimeAxisSetter {
	
	/**
	 * One minute in milliseconds.
	 */
	public static final double MS_TO_MIN = 60000;
	
	/**
	 * One second in milliseconds.
	 */
	public static final double MS_TO_SEC = 1000;
	private static final double LABELS_PX_GAP = 45;
	private static final int MAX_LABELS = 28;
	private static final int MIN_WIDTH = 50;
	private static final int TICK_LABEL_GAP = 5;
	private static final int SCROLLER_HEIGTH = 7;
	private static final int SCROLLER_MIN_WIDTH = 20;

	/**
	 * The time axis.
	 */
	private final NumberAxis axis = new NumberAxis();
	
	/**
	 * The navigation bar panel.
	 */
	private final AnchorPane navigator = new AnchorPane();
	
	/**
	 * The navigator bar.
	 */
	private final Rectangle scroller = new Rectangle();
	
	/**
	 * The time length of the project.
	 */
	private double prLength;
	
	/**
	 * The time interval displayed on axis.
	 */
	private double timeDelta;
	
	/**
	 * Create a new time axis and the associated setter.
	 * 
	 * @param projectLength the initial project length in ms.
	 */
	public TimeAxisSetter(final double projectLength) {
		//set ptoject length
		this.prLength = projectLength;
		App.getData().getProjectLenghtProperty().addListener((obs, old, n) -> {
			this.setProjectLength(n.doubleValue());
		});
	//---------NUMBER AXIS-------------
		axis.setAnimated(false);
		axis.setAutoRanging(false);
		axis.setSide(Side.TOP);
		axis.setMinWidth(MIN_WIDTH);
		//set the range to 100% prLength
		resetRange();
		axis.setTickLabelGap(TICK_LABEL_GAP);
		//------CALCULATE TICK WHEN RESIZE AXIS-------
		axis.needsLayoutProperty().addListener((obs, old, needsLayout) -> {
			calculateTicks(); 
			updateScroller();
		});
		//-----PRINT TIME INSTEAD OF MILLISECONDS-----
		axis.setTickLabelFormatter(new NumberFormatConverter());
		//------SET PRECISION NAVIGATION---------
		axis.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.SECONDARY)) {
					class AxisMenu extends Pane {
						private static final int POPUP_PADDING = 12;
						private final TextField lb = new TextField();
						private final TextField ub = new TextField();
						private final NumberFormatConverter fc = new NumberFormatConverter();
						AxisMenu() {
							//buttons
							HBox buttons = new HBox();
							Button reset = new Button("RESET");
							reset.setOnAction(a -> {
								resetRange();
								this.getScene().getWindow().hide();
							});
							Button ok = new Button("OK");
							ok.setOnAction(a -> {
								setBounds();
								this.getScene().getWindow().hide();
							});
							buttons.getChildren().addAll(reset, ok);
							//lower bound
							HBox lowerBoundSetter = new HBox();	
							lb.setPromptText(fc.toString(axis.getLowerBound()));
							lb.setTextFormatter(new TextFormatter<>(fc.getFormatterUnaryOperator()));
							lowerBoundSetter.getChildren().addAll(new Label("from: "), lb);
							//upper bound
							HBox upperBoundSetter = new HBox();
							ub.setPromptText(fc.toString(axis.getUpperBound()));
							ub.setTextFormatter(new TextFormatter<>(fc.getFormatterUnaryOperator()));
							upperBoundSetter.getChildren().addAll(new Label("to: "), ub);
							//pop-up
							VBox v = new VBox();
							v.setPadding(new Insets(POPUP_PADDING));
							v.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, null, null)));
							v.setBackground(new Background(new BackgroundFill(Paint.valueOf("#999999"), null, null)));
							v.getChildren().addAll(new Label("Set axis range"), lowerBoundSetter, upperBoundSetter, buttons);
							this.getChildren().add(v);
						}
						void setBounds() {
							if (lb.getText().isBlank() || ub.getText().isBlank()) {
								return;
							}
							StringConverter<Number> sc = axis.getTickLabelFormatter();
							setRange(sc.fromString(lb.getText()).doubleValue(), sc.fromString(ub.getText()).doubleValue());	
						}
					}
					Pane p = new AxisMenu();
					Scene s = new Scene(p);
					Stage stage = new Stage();
					stage.setScene(s);
					stage.initModality(Modality.WINDOW_MODAL);
					stage.setTitle("Precision zoom");
					stage.setX(e.getScreenX());
					stage.setY(e.getScreenY());
					stage.initOwner(axis.getScene().getWindow());
					stage.showAndWait();
				}
			});
	//----------SCROLLER----------------
		scroller.setFill(Paint.valueOf("#444444"));
		AnchorPane.setBottomAnchor(scroller, 0.0);
		AnchorPane.setTopAnchor(scroller, 0.0);
		scroller.setHeight(SCROLLER_HEIGTH);
		scroller.setArcHeight(SCROLLER_HEIGTH - 2);
		scroller.setArcWidth(SCROLLER_HEIGTH - 2);
		navigator.getChildren().add(scroller);
		//------DRAG RESIZE AND NAVIGATE-----------
		TimeAxisDragNavigator.makeNavigable(this);
	}

	//---------FX COMPONENTS GETTERS---------------

	public NumberAxis getAxis() {
		return axis;
	}
	
	public AnchorPane getNavigator() {
		return navigator;
	}

	public double getScrollerX() {
		return AnchorPane.getLeftAnchor(scroller);
	}

	public double getScrollerWidth() {
		return scroller.getWidth();
	}
	
	//---------CALCULATE AXIS---------------------
	/**
	 * Calculate tick unit for major and minor ticks.
	 * <p>The final unit will be calculated from the actual axis length and it's value range. 
	 * If possible the unit will be also a multiple of standard time units like seconds or minutes.
	 * Needs to be called every time number axis needs layout;
	 */
	private void calculateTicks() {
		Long maxLabels = Double.valueOf(axis.getWidth() / LABELS_PX_GAP).longValue();
		double minTickUnit = timeDelta / Math.min(MAX_LABELS, maxLabels);
		//round to multiples of min or sec.
		double tick;
		double mTCount;
		if (minTickUnit <= 10) {
			tick = 10;
			mTCount = 10;
		} else if (minTickUnit <= 100) {
			tick = 100;
			mTCount = 10;
		} else if (minTickUnit <= MS_TO_SEC) {
			tick = MS_TO_SEC;
			mTCount = 10;
		} else if (minTickUnit <= MS_TO_MIN) {
			tick = minTickUnit - (minTickUnit % MS_TO_SEC);
			mTCount = tick / MS_TO_SEC;
		} else {
			tick = minTickUnit - (minTickUnit % MS_TO_MIN);
			mTCount = tick / MS_TO_MIN;
		}
		//set
		axis.setTickUnit(Double.valueOf(tick).longValue());
		axis.setMinorTickCount(Double.valueOf(mTCount).intValue());
	}
	
	/**
	 * Set a new range for the timeline. Responsable of update scroller position.
	 * If tick unit is bigger or equals MS_TO_SEC, lb and ub will be rounded to integer sec value.
	 * @param lowerBound
	 * @param upperBound
	 */
	public void setRange(final double lowerBound, final double upperBound) {
		if (lowerBound < 0 || upperBound > prLength || lowerBound >= upperBound) {
			throw new IllegalArgumentException("Lower and upper bounds must be between 0 and project length.");
		}
		double newLB = lowerBound;
		double newUB = upperBound;
		final int seconds = 5;
		if (upperBound - lowerBound >= MS_TO_SEC * seconds) {
			newLB = Math.round(newLB / MS_TO_SEC) * MS_TO_SEC;
			newUB = Math.round(newUB / MS_TO_SEC) * MS_TO_SEC;
		} else if (upperBound - lowerBound >= MS_TO_SEC) {
			newLB = Math.round(newLB / 100) * 100;
			newUB = Math.round(newUB / 100) * 100;
		}
		axis.setTickUnit(Double.MAX_VALUE);
		axis.setLowerBound(newLB);
		axis.setUpperBound(newUB);
		timeDelta = axis.getUpperBound() - axis.getLowerBound();
		axis.requestAxisLayout();
	}
	
	public void resetRange() {
		setRange(0, prLength);
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
		scroller.setWidth(axis.getWidth() * calculatePercent(prLength, timeDelta));
		if (scroller.getWidth() < SCROLLER_MIN_WIDTH) {
			scroller.setWidth(SCROLLER_MIN_WIDTH);
		}
		//position
		double pos = axis.getWidth() * calculatePercent(prLength, axis.getLowerBound());
		AnchorPane.setLeftAnchor(scroller, pos);
	}
	
	/**
	 * Return a number between 0 and 1. ex: 0.2 = 20%;
	 * @param total
	 * @param part
	 * @return
	 */
	private double calculatePercent(final double total, final double part) {
		return part / total;
	}
	
	//----PROJECT LENGTH------
	public double getProjectLength() {
		return prLength;
	}
	
	public void setProjectLength(final double prLength) {
		this.prLength = prLength;
		calculateTicks();
		updateScroller();
	}
}

