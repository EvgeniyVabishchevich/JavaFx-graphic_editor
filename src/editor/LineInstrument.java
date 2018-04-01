package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.WritableImage;

public class LineInstrument implements  Instrument {

    /**
     * Указывает зажата ли ЛКМ
     */
    private boolean mousePressed;

    /**
     * Координатые, указывающие на начало отрезка
     */
    private double startX, startY;

    /**
     * Картинка, до начала рисования отрезка
     */
    private WritableImage startWritableImage;

    /**
     * Нажат ли шифт
     */
    private boolean shiftDown = false;

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(canvas.getInstrumentPanel().getCurrentMainColor());
        gc.setLineWidth(canvas.getInstrumentPanel().getCurrentThickness());

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
        {
            mousePressed = true;
            MouseEvent mouseEvent = (MouseEvent) event;
            startX = mouseEvent.getX();
            startY = mouseEvent.getY();
            startWritableImage = canvas.snapshot(new SnapshotParameters(), null);
        }

        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && mousePressed)
        {
           gc.drawImage(startWritableImage, 0, 0);
            MouseEvent mouseEvent = (MouseEvent) event;
            if(shiftDown)
            gc.strokeLine(startX, startY, mouseEvent.getX(), startY);
            else gc.strokeLine(startX, startY, mouseEvent.getX(), mouseEvent.getY());
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
