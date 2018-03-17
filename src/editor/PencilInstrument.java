package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PencilInstrument implements Instrument {

    private int pencilThickness;

    private boolean shiftDown = false;

    private double startY;

    private boolean mousePressed;

    PencilInstrument()
    {
        pencilThickness = 3;
    }
    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas)
    {
            GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(canvas.getInstrumentPanel().getCurrentMainColor());

            if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
            {
                mousePressed = true;
                MouseEvent mouseEvent = (MouseEvent) event;
                startY = mouseEvent.getY();
            }

            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && mousePressed) {
                if (!shiftDown) {
                    MouseEvent mouseEvent = (MouseEvent) event;
                    gc.fillOval(mouseEvent.getX(), mouseEvent.getY(), pencilThickness, pencilThickness);
                    startY = mouseEvent.getY();
                }
                if (shiftDown) {
                    MouseEvent mouseEvent = (MouseEvent) event;
                    gc.fillOval(mouseEvent.getX(), startY, pencilThickness, pencilThickness);
                }
            }

            if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
            {
                canvas.getSnapshot(canvas);
                mousePressed = false;
            }

            if (event.getEventType() == KeyEvent.KEY_PRESSED || event.getEventType() == KeyEvent.KEY_RELEASED)
            {
                KeyEvent keyEvent = (KeyEvent) event;
                shiftDown = (keyEvent.isShiftDown()) ? true : false;
            }
    }
}
