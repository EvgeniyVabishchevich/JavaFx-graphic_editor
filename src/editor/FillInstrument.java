package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.input.InputEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * FillInstrument - класс инструмента заливки
 */
public class FillInstrument implements Instrument {

    public Color fillColor = Color.WHITE;

    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            MouseEvent mouseEvent = (MouseEvent) event;
            paint(new Pixel((int) mouseEvent.getX(), (int) mouseEvent.getY()), canvas);
        }

        if (event.getEventType() == KeyEvent.KEY_PRESSED) {

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

            if (startPixel.x + 1 < canvas.getWidth()) pixels.add(new Pixel(startPixel.x + 1, startPixel.y));

            if (startPixel.x - 1 > 0) pixels.add(new Pixel(startPixel.x - 1, startPixel.y));

            if (startPixel.y + 1 < canvas.getHeight()) pixels.add(new Pixel(startPixel.x, startPixel.y + 1));

            if (startPixel.y - 1 > 0) pixels.add(new Pixel(startPixel.x, startPixel.y - 1));
        }
        canvas.getGraphicsContext2D().drawImage(writableImage, 0, 0);
    }
}
