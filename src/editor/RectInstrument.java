package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
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
            if(mouseEvent.getX() > startX && mouseEvent.getY() > startY)
                canvas.getGraphicsContext2D().strokeRect(startX, startY, mouseEvent.getX() - startX, mouseEvent.getY() - startY);
            if(mouseEvent.getX() < startX && mouseEvent.getY() < startY)
                canvas.getGraphicsContext2D().strokeRect(mouseEvent.getX(), mouseEvent.getY(), startX - mouseEvent.getX(), startY - mouseEvent.getY());
            if(mouseEvent.getX() > startX && mouseEvent.getY() < startY)
                canvas.getGraphicsContext2D().strokeRect(startX, mouseEvent.getY(), mouseEvent.getX() - startX, startY - mouseEvent.getY());
            if(mouseEvent.getX() < startX && mouseEvent.getY() > startY)
                canvas.getGraphicsContext2D().strokeRect(mouseEvent.getX(), startY, startX - mouseEvent.getX(), mouseEvent.getY() - startY);
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
        {
            mousePressed = false;
        }
    }
}
