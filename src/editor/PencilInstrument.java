package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PencilInstrument implements Instrument {

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

    private double previousGetX = -1;
    private double previousGetY = -1;

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas)
    {
            setFill(canvas);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(canvas.getInstrumentPanel().getCurrentThickness());

            if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
            {
                startWritableImage = canvas.snapshot(new SnapshotParameters(), null);
                mousePressed = true;
                MouseEvent mouseEvent = (MouseEvent) event;
                startY = mouseEvent.getY();

                previousGetX = mouseEvent.getX();
                previousGetY = mouseEvent.getY();

            }

            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && mousePressed) {
                MouseEvent mouseEvent = (MouseEvent) event;

                if(previousGetX != -1)gc.strokeLine(previousGetX, previousGetY, mouseEvent.getX(), mouseEvent.getY());
                previousGetX = mouseEvent.getX();
                previousGetY = mouseEvent.getY();
            }

            if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
            {
                canvas.addSnapshot(startWritableImage);
                mousePressed = false;
                previousGetX = -1;
            }

            if (event.getEventType() == KeyEvent.KEY_PRESSED || event.getEventType() == KeyEvent.KEY_RELEASED)
            {
                KeyEvent keyEvent = (KeyEvent) event;
                shiftDown = (keyEvent.isShiftDown());
            }
    }

    public void setFill(EditorCanvas canvas)
    {
        canvas.getGraphicsContext2D().setStroke(canvas.getInstrumentPanel().getCurrentMainColor());
    }
}
