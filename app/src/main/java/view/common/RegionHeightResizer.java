package view.common;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * {@link RegionHeightResizer} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 * <p>
 * Only height resizing is currently implemented.
 */
public final class RegionHeightResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 5;

    private final Region region;

    private double y;

    private boolean initMinHeight;

    private boolean dragging;

    private RegionHeightResizer(final Region aRegion) {
        region = aRegion;
    }

    public static void makeResizable(final Region region) {
        final RegionHeightResizer resizer = new RegionHeightResizer(region);

        region.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                resizer.mousePressed(event);
            }
        });
        region.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                resizer.mouseDragged(event);
            }
        });
        region.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                resizer.mouseOver(event);
            }
        });
        region.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                resizer.mouseReleased(event);
            }
        });
    }

    protected void mouseReleased(final MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(final MouseEvent event) {
        if (isInDraggableZone(event) || dragging) {
            region.setCursor(Cursor.S_RESIZE);
        } else {
            region.setCursor(Cursor.DEFAULT);
        }
    }

    protected boolean isInDraggableZone(final MouseEvent event) {
        return event.getY() > (region.getHeight() - RESIZE_MARGIN);
    }

    protected void mouseDragged(final MouseEvent event) {
        if (!dragging) {
            return;
        }

        double mousey = event.getY();

        double newHeight = region.getMinHeight() + (mousey - y);

        region.setMinHeight(newHeight);

        y = mousey;
    }

    protected void mousePressed(final MouseEvent event) {
        // ignore clicks outside of the draggable margin
        if (!isInDraggableZone(event)) {
            return;
        }
        dragging = true;
        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            initMinHeight = true;
        }
        y = event.getY();
    }
}
