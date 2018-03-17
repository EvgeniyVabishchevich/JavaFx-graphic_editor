package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PencilInstrument implements Instrument {

    /**
     * Толщина карандаша
     */
    private int pencilThickness;

    /**
     * Нажат ли шифт
     */
    private boolean shiftDown = false;

    /**
     * Координата по y, первоначального нажатия мышки
     */
    private double startY;

    /**
     * Нажата ли мышка
     */
    private boolean mousePressed;

    /**
     * Картинка, до начала рисования карандашом
     */
    private WritableImage startWritableImage;

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
                startWritableImage = canvas.snapshot(new SnapshotParameters(), null);
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
                canvas.addSnapshot(startWritableImage);
                mousePressed = false;
            }

            if (event.getEventType() == KeyEvent.KEY_PRESSED || event.getEventType() == KeyEvent.KEY_RELEASED)
            {
                KeyEvent keyEvent = (KeyEvent) event;
                shiftDown = (keyEvent.isShiftDown());
            }
    }
}
