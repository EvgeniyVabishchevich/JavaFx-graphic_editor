package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * FillInstrument - класс инструмента заливки
 */
public class FillInstrument implements Instrument {

    /**
     * цвет заливки
     */
    private Color fillColor;

    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {

        fillColor = canvas.getInstrumentPanel().getCurrentMainColor();

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
        {
            MouseEvent mouseEvent = (MouseEvent) event;
            canvas.addSnapshot(canvas.snapshot(new SnapshotParameters(), null));
            paint(new Pixel((int) mouseEvent.getX(), (int) mouseEvent.getY()), canvas);
        }
    }

    /**
     * Окрашивает выбранный участок холста
     * @param startPixel Пиксель на который нажали заливкой
     * @param canvas холст который будет изменяться
     */
    public void paint(Pixel startPixel, Canvas canvas) {
        WritableImage writableImage = canvas.snapshot(new SnapshotParameters(), null);
        PixelReader pixelReader = writableImage.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        Color startColor = pixelReader.getColor(startPixel.x, startPixel.y);
        if (startColor.equals(fillColor)) return;

        ConcurrentLinkedQueue<Pixel> pixels = new ConcurrentLinkedQueue();

        pixels.add(startPixel);

        while (!pixels.isEmpty()) {
            startPixel = pixels.poll();
            if (!startColor.equals(pixelReader.getColor(startPixel.x, startPixel.y))) continue;
            pixelWriter.setColor(startPixel.x, startPixel.y, fillColor);

            // Правый
            if (startPixel.x + 1 < canvas.getWidth()) pixels.add(new Pixel(startPixel.x + 1, startPixel.y));

            // Левый
            if (startPixel.x - 1 > 0) pixels.add(new Pixel(startPixel.x - 1, startPixel.y));

            // Верхний
            if (startPixel.y + 1 < canvas.getHeight()) pixels.add(new Pixel(startPixel.x, startPixel.y + 1));

            // Нижний
            if (startPixel.y - 1 > 0) pixels.add(new Pixel(startPixel.x, startPixel.y - 1));
        }
        canvas.getGraphicsContext2D().drawImage(writableImage, 0, 0);
    }
}
