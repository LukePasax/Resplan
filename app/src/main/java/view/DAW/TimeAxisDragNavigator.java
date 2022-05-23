package view.DAW;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;

public class TimeAxisDragNavigator {

    private final TimeAxisSetter setter;
    private final NumberAxis axis;

    private double initialY;
    private double initialTime;
    private double initialLowerBound;
    private double initialUpperBound;

    private boolean dragging;

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
        timeAxisSetter.getNavigator().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mousePressed(event);
            }});
        timeAxisSetter.getNavigator().setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseDragged(event);
            }});
        timeAxisSetter.getNavigator().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                navigator.mouseOver(event);
            }});
        timeAxisSetter.getNavigator().setOnMouseReleased(new EventHandler<MouseEvent>() {
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
    		//scroll event
    		if(isInScrollerDraggableZone(event)) {
    			setter.getNavigator().setCursor(Cursor.OPEN_HAND);
	        }
	        else if(dragging) {
	            setter.getNavigator().setCursor(Cursor.CLOSED_HAND);
	        } else {
	        	setter.getNavigator().setCursor(Cursor.HAND);
	        }
    	}
       
    }

    protected boolean isInScrollerDraggableZone(MouseEvent event) {
        return (event.getX() > setter.getScrollerX() && event.getX() < (setter.getScrollerX()+setter.getScrollerWidth()));
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
        	double ratio = event.getX()/axis.getWidth();
			double actualTime = setter.getProjectLength()*ratio;
        	double vSpostamento = actualTime-initialTime;
        	double lb = initialLowerBound + vSpostamento;
        	double ub = initialUpperBound + vSpostamento;
        	if(lb<0) {
        		lb=0;
        	}
        	if(ub > setter.getProjectLength()) {
        		ub = setter.getProjectLength();
        	}
        	if(lb<ub) {
        		setter.setRange(lb, ub);
        	}
        }
    }

    protected void mousePressed(MouseEvent event) {
    	//zoom event
    	if(event.getSource().equals(axis)) {
    		//reset zoom
    		if(event.getClickCount()==2) {
    			setter.setRange(0, setter.getProjectLength());
            	return;
    		}
    		dragging = true;
    		initialY = event.getY();
    		initialTime = axis.getValueForDisplay(event.getX()).doubleValue();
    		initialLowerBound = axis.getLowerBound();
    		initialUpperBound = axis.getUpperBound();
    	} else {
    		//scroll event
    		if(isInScrollerDraggableZone(event)) {
    			//scroll
    			dragging = true;
    			initialTime = axis.getValueForDisplay(event.getX()).doubleValue();
    			initialLowerBound = axis.getLowerBound();
        		initialUpperBound = axis.getUpperBound();
    		} else {
    			//instant move
    			double ratio = event.getX()/axis.getWidth();
    			double clickedTime = setter.getProjectLength()*ratio;
    			double timeDelta = axis.getUpperBound()-axis.getLowerBound();
    			double lb = clickedTime-(timeDelta/2)<0 ? 0 : clickedTime-(timeDelta/2);
    			double ub = clickedTime+(timeDelta/2)>setter.getProjectLength() ? setter.getProjectLength() : clickedTime+(timeDelta/2);
    			setter.setRange(lb, ub);
    		}
    	}
    }
}
