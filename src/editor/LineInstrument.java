package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


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

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {
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
            canvas.getGraphicsContext2D().drawImage(startWritableImage, 0, 0);
            MouseEvent mouseEvent = (MouseEvent) event;
            canvas.getGraphicsContext2D().strokeLine(startX, startY, mouseEvent.getX(), mouseEvent.getY());
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
        {
            mousePressed = false;
        }
    }
}
