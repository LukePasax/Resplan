package view.common;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public final class ResizeHelper {

    private static boolean isScrollbar;
    private ResizeHelper() { }

    public static void addResizeListener(final Stage stage) {
        addResizeListener(stage, 1, 1, Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public static void addResizeListener(final Stage stage, final double minWidth, final double minHeight, final double maxWidth, final double maxHeight) {
        final ResizeListener resizeListener = new ResizeListener(stage);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);

        resizeListener.setMinWidth(minWidth);
        resizeListener.setMinHeight(minHeight);
        resizeListener.setMaxWidth(maxWidth);
        resizeListener.setMaxHeight(maxHeight);


        final ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (final Node child : children) {
            if (child instanceof ScrollBar) {
                isScrollbar = true;
            } else {
                isScrollbar = false;
                addListenerDeeply(child, resizeListener);
            }
        }
    }

    private static void addListenerDeeply(final Node node, final EventHandler<MouseEvent> listener) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
        if (node instanceof Parent) {
            final Parent parent = (Parent) node;
            final ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (final Node child : children) {
                if (child instanceof ScrollBar) {
                    isScrollbar = true;
                } else {
                    isScrollbar = false;
                    addListenerDeeply(child, listener);
                }
            }
        }
    }

    static class ResizeListener implements EventHandler<MouseEvent> {
        private final Stage stage;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private boolean resizing = true;
        private double startX;
        private double startY;
        private double screenOffsetX;
        private double screenOffsetY;

        // Max and min sizes for controlled stage
        private double minWidth;
        private double maxWidth;
        private double minHeight;
        private double maxHeight;

        ResizeListener(final Stage stage) {
            this.stage = stage;
        }

        public void setMinWidth(final double minWidth) {
            this.minWidth = minWidth;
        }

        public void setMaxWidth(final double maxWidth) {
            this.maxWidth = maxWidth;
        }

        public void setMinHeight(final double minHeight) {
            this.minHeight = minHeight;
        }

        public void setMaxHeight(final double maxHeight) {
            this.maxHeight = maxHeight;
        }

        @Override
        public void handle(final MouseEvent mouseEvent) {
            final EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            final Scene scene = stage.getScene();

            final double mouseEventX = mouseEvent.getSceneX(),
                    mouseEventY = mouseEvent.getSceneY(),
                    sceneWidth = scene.getWidth(),
                    sceneHeight = scene.getHeight();

            final int border = 4;
            if (MouseEvent.MOUSE_MOVED.equals(mouseEventType) && !stage.isMaximized()) {
                if (mouseEventX < border && mouseEventY < border) {
                    cursorEvent = Cursor.NW_RESIZE;
                } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.SW_RESIZE;
                } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                    cursorEvent = Cursor.NE_RESIZE;
                } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.SE_RESIZE;
                } else if (mouseEventX < border) {
                    cursorEvent = Cursor.W_RESIZE;
                } else if (mouseEventX > sceneWidth - border) {
                    cursorEvent = Cursor.E_RESIZE;
                } else if (mouseEventY < border) {
                    cursorEvent = Cursor.N_RESIZE;
                } else if (mouseEventY > sceneHeight - border) {
                    cursorEvent = Cursor.S_RESIZE;
                } else {
                    cursorEvent = Cursor.DEFAULT;
                }
                scene.setCursor(cursorEvent);
            } else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
                scene.setCursor(Cursor.DEFAULT);
            } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
                startX = stage.getWidth() - mouseEventX;
                startY = stage.getHeight() - mouseEventY;
            } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
                if (!Cursor.DEFAULT.equals(cursorEvent)) {
                    resizing = true;
                    if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
                        final double minHeight = stage.getMinHeight() > border * 2 ? stage.getMinHeight() : border * 2;
                        if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent)
                                || Cursor.NE_RESIZE.equals(cursorEvent)) {
                            if (stage.getHeight() > minHeight || mouseEventY < 0) {
                                setStageHeight(stage.getY() - mouseEvent.getScreenY() + stage.getHeight());
                                stage.setY(mouseEvent.getScreenY());
                            }
                        } else if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) {
                                setStageHeight(mouseEventY + startY);
                        }
                    }

                    if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
                        final double minWidth = stage.getMinWidth() > border * 2 ? stage.getMinWidth() : border * 2;
                        if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent)
                                || Cursor.SW_RESIZE.equals(cursorEvent)) {
                            if (stage.getWidth() > minWidth || mouseEventX < 0) {
                                setStageWidth(stage.getX() - mouseEvent.getScreenX() + stage.getWidth());
                                stage.setX(mouseEvent.getScreenX());
                            }
                        } else {
                            if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) {
                                setStageWidth(mouseEventX + startX);
                            }
                        }
                    }
                    resizing = false;
                }
            }

            if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType) && Cursor.DEFAULT.equals(cursorEvent)) {
                resizing = false;
                screenOffsetX = stage.getX() - mouseEvent.getScreenX();
                screenOffsetY = stage.getY() - mouseEvent.getScreenY();

            }

            if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && Cursor.DEFAULT.equals(cursorEvent) && !resizing) {
                stage.setX(mouseEvent.getScreenX() + screenOffsetX);
                stage.setY(mouseEvent.getScreenY() + screenOffsetY);

            }

        }

        private void setStageWidth(final double width) {
            double newWidth = Math.min(width, maxWidth);
            newWidth = Math.max(newWidth, minWidth);
            stage.setWidth(newWidth);
        }

        private void setStageHeight(final double height) {
            double newHeight = Math.min(height, maxHeight);
            newHeight = Math.max(newHeight, minHeight);
            stage.setHeight(newHeight);
        }

    }
}
