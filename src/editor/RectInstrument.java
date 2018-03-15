package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class RectInstrument implements Instrument {
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

    private boolean shiftDown;

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(canvas.getInstrumentPanel().getCurrentMainColor());

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
            double width = Math.abs(startX - mouseEvent.getX());
            double height = Math.abs(startY - mouseEvent.getY());
            double topLeftX = Math.min(startX, mouseEvent.getX());
            double topLeftY = Math.min(startY, mouseEvent.getY());
            if(shiftDown)
            {
                width = height = Math.min(width, height);
                topLeftX = (mouseEvent.getX() - startX > 0) ? topLeftX : Math.max(startX, mouseEvent.getX()) - width;
                topLeftY = (mouseEvent.getY() - startY > 0) ? topLeftY : Math.max(startY, mouseEvent.getY()) - height;
            }

            gc.strokeRect(topLeftX, topLeftY, width, height);
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
        {
            mousePressed = false;
        }

        if (event.getEventType() == KeyEvent.KEY_PRESSED || event.getEventType() == KeyEvent.KEY_RELEASED)
        {
            KeyEvent keyEvent = (KeyEvent) event;
            shiftDown = (keyEvent.isShiftDown()) ? true : false;
        }
    }
}
