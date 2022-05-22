package view.DAW;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class TimeAxisDragNavigator {
    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 5;

    private final TimeAxisSetter setter;
    private final NumberAxis axis;

    private double initialY;
    private double initialTime;
    private double initialTimeX;
    private double initialLowerBound;
    private double initialLowerBoundX;
    private double initialUpperBound;
    private double initialUpperBoundY;

    private boolean dragging;
    
    private final static String SEP = System.getProperty("file.separator");
    
    //private ImageCursor magnifier = new ImageCursor(new Image(System.getProperty("user.dir") + SEP + "src" + SEP + "main" + SEP + "resources" + SEP + "images" + SEP + "magnifier.png"));

    private TimeAxisDragNavigator(TimeAxisSetter timeAxisSetter) {
        setter = timeAxisSetter;
        axis = timeAxisSetter.getAxis();
    }

    public static void makeNavigable(TimeAxisSetter timeAxisSetter) {
        final TimeAxisDragNavigator navigator = new TimeAxisDragNavigator(timeAxisSetter);

        //on events for zoom (time axis)
        timeAxisSetter.getAxis().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mousePressed(event);
            }});
        timeAxisSetter.getAxis().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseDragged(event);
            }});
        timeAxisSetter.getAxis().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseOver(event);
            }});
        timeAxisSetter.getAxis().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseReleased(event);
            }});
        
        //on events for scrolling (scroller)
        timeAxisSetter.getScroller().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mousePressed(event);
            }});
        timeAxisSetter.getScroller().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseDragged(event);
            }});
        timeAxisSetter.getScroller().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseOver(event);
            }});
        timeAxisSetter.getScroller().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseReleased(event);
            }});
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        axis.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
    	if(event.getSource().equals(axis)) {
    		//zoom event
    		axis.setCursor(Cursor.V_RESIZE);
    	} else {
    		//scroll event TODO
    		if(isInScrollerDraggableZone(event) || dragging) {
            setter.getScroller().setCursor(Cursor.S_RESIZE);
	        }
	        else {
	            //setter.setCursor(Cursor.DEFAULT);
	        }
    	}
       
    }

    protected boolean isInScrollerDraggableZone(MouseEvent event) {
    	//TODO
        return event.getY() > (setter.getScroller().getWidth() - RESIZE_MARGIN);
    }

    protected void mouseDragged(MouseEvent event) {
    	
        if(!dragging) {
            return;
        }
        
        if(event.getSource().equals(axis)) {
        	//zoom event
        	double vSpostamento = event.getY()-initialY;
        	double newLow; 
        	double newUp;
        	double ratio; 
        	if(initialLowerBound==0 && vSpostamento<0) {
        		newLow = 0;
        		newUp = axis.getValueForDisplay(axis.getDisplayPosition(initialUpperBound)-vSpostamento).doubleValue();
        	
        	} else {
        		newLow = axis.getValueForDisplay(axis.getDisplayPosition(initialLowerBound)+vSpostamento).doubleValue();
	        	ratio = (initialTime-initialLowerBound)/(initialUpperBound-initialLowerBound);  	
	        	newUp = initialTime+((initialTime-newLow)*ratio*(1-ratio));
        	}
        	if(newLow<0) {
        		newLow = 0;
        	}
        	if(newUp>setter.getProjectLength()) {
        		newUp = setter.getProjectLength();
        	}
        	if(newLow<newUp) {
        		setter.setRange(newLow, newUp);
        	}
        } else {
        	//scroll event
        }
/*
        double mousey = event.getY();

        double newHeight = setter.getMinHeight() + (mousey - y);

        setter.setMinHeight(newHeight);

        y = mousey;*/
    }

    protected void mousePressed(MouseEvent event) {
    	//zoom event
    	if(event.getSource().equals(axis)) {
    		//reset zoom
    		if(event.getClickCount()==2) {
    			axis.setLowerBound(0);
            	axis.setUpperBound(setter.getProjectLength());
            	return;
    		}
    		dragging = true;
    		initialY = event.getY();
    		initialTime = axis.getValueForDisplay(event.getX()).doubleValue();
    		initialTimeX = axis.getDisplayPosition(initialTime);
    		initialLowerBound = axis.getLowerBound();
    		initialUpperBound = axis.getUpperBound();
    	} else {
    		//scroll event
    	}
 /*       // ignore clicks outside of the draggable margin
        if(!isInScrollerDraggableZone(event)) {
            return;
        }

        dragging = true;

        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            setter.setMinHeight(setter.getHeight());
            initMinHeight = true;
        }

        y = event.getY();*/
    }
}
