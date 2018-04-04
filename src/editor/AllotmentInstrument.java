package editor;

import javafx.scene.paint.Color;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AllotmentInstrument implements Instrument {

    /**
     * Указывает зажата ли ЛКМ
     */
    private boolean mousePressed;

    /**
     * Координатые, указывающие на начало отрезка
     */
    private double startX, startY;

    /**
     * Картинка, до начала рисования прямоугольника
     */
    private WritableImage startWritableImage = null;

    private boolean shiftDown;

    /**
     * текущие координаты верхнего левого угла области выделения, ширина, высота
     */
    private double x1 = -1, y1 = -1, allotmentWidth, allotmentHeight;

    /**
     * Нажата ли мышка на области выделения
     */
    private boolean pressedOnAllotment;

    /**
     * изображение области выделения
     */
    private WritableImage allotmentImage;

    /**
     * Начальные координаты верхнего левого угла областы выделения
     */
    private double x,y;

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(canvas.getInstrumentPanel().getCurrentMainColor());

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
        {
            MouseEvent mouseEvent = (MouseEvent) event;
            if(x1 > -1 && inAllotment(mouseEvent.getX(), mouseEvent.getY()))
            {
                pressedOnAllotment = true;
                System.out.println("pressed");
                if(startWritableImage == null)startWritableImage = canvas.snapshot(new SnapshotParameters(), null);
                startX = mouseEvent.getX();
                startY = mouseEvent.getY();
            }
            else
            {
                if(startWritableImage!= null)
                {
                    gc.drawImage(startWritableImage, 0, 0);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x, y, allotmentWidth, allotmentHeight);
                    gc.drawImage(allotmentImage, x1, y1, allotmentWidth, allotmentHeight);
                }
                mousePressed = true;
                startX = mouseEvent.getX();
                startY = mouseEvent.getY();
                startWritableImage = canvas.snapshot(new SnapshotParameters(), null);
            }
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

            x = topLeftX;
            y = topLeftY;
            x1 = topLeftX;
            y1 = topLeftY;
            allotmentWidth = (x1 + width < canvas.getWidth()) ? width : canvas.getWidth() - x1;
            allotmentHeight = (y1 + height < canvas.getHeight()) ? height : canvas.getHeight() - y1;
            allotmentImage = canvas.snapshot(new SnapshotParameters(), null);
            PixelReader pixelReader = allotmentImage.getPixelReader();
            if(allotmentWidth > 0 && allotmentHeight > 0)allotmentImage = new WritableImage(pixelReader, (int)x1, (int)y1, (int)allotmentWidth, (int)allotmentHeight);
            gc.setLineWidth(1);
            gc.setStroke(Color.RED);
            gc.strokeRect(topLeftX + 1, topLeftY + 1, allotmentWidth - 2, allotmentHeight - 2);
        }

        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && pressedOnAllotment)
        {
            MouseEvent mouseEvent = (MouseEvent) event;
            gc.drawImage(startWritableImage, 0, 0);
            gc.setFill(Color.WHITE);
            gc.fillRect(x, y, allotmentWidth, allotmentHeight);
            gc.drawImage(allotmentImage, mouseEvent.getX() - (startX - x1), mouseEvent.getY() - (startY - y1));
            gc.setLineWidth(1);
            gc.setStroke(Color.RED);
            gc.strokeRect(mouseEvent.getX() - (startX - x1) + 1, mouseEvent.getY() - (startY - y1) + 1, allotmentWidth - 2, allotmentHeight - 2);
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
        {
            if(pressedOnAllotment)
            {
                MouseEvent mouseEvent = (MouseEvent) event;
                x1 = mouseEvent.getX() - (startX - x1);
                y1 = mouseEvent.getY() - (startY - y1);
                gc.drawImage(startWritableImage, 0, 0);
                gc.setFill(Color.WHITE);
                gc.fillRect(x, y, allotmentWidth, allotmentHeight);
                gc.drawImage(allotmentImage, x1, y1, allotmentWidth, allotmentHeight);
                gc.setStroke(Color.RED);
                gc.strokeRect(x1 + 1, y1 + 1, allotmentWidth, allotmentHeight);
                pressedOnAllotment = false;
            }
            canvas.addSnapshot(startWritableImage);
            if(mousePressed) startWritableImage = null;
            mousePressed = false;
        }

        if (event.getEventType() == KeyEvent.KEY_PRESSED || event.getEventType() == KeyEvent.KEY_RELEASED)
        {
            KeyEvent keyEvent = (KeyEvent) event;
            shiftDown = (keyEvent.isShiftDown());
        }


    }

    @Override
    public void attached(EditorCanvas canvas) {

    }

    @Override
    public void detached(EditorCanvas canvas) {

    }

    public boolean inAllotment(double x, double y)
    {
        return ( x > x1 && x < x1 + allotmentWidth && y > y1 && y < y1 + allotmentHeight) ? true : false;
    }

}
