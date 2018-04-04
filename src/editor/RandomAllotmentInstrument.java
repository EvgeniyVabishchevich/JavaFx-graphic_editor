package editor;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

import java.util.ArrayList;

public class RandomAllotmentInstrument implements Instrument {

    /**
     * Нажата ли мышка
     */
    private boolean mousePressed;

    /**
     * Нажатие мышки на выделении
     */
    private boolean pressedOnAllotment = false;

    /**
     * Изображение canvas, до текущего действия
     */
    private WritableImage startWritableImage;

    /**
     * Вершины выделения
     */
    private ArrayList<Pixel> vertexes = new ArrayList<Pixel>();

    /**
     * Пиксели выделения
     */
    private ArrayList<Pixel> pixels = new ArrayList<Pixel>();

    /**
     * Координаты пикселей выделения, до его перемещения
     */
    private ArrayList<Pixel> startPixels = new ArrayList<Pixel>();

    private double pressedX = -1;
    private double pressedY = -1;

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
        {
            mousePressed = true;
            MouseEvent mouseEvent = (MouseEvent) event;
            pressedX = mouseEvent.getX();
            pressedY = mouseEvent.getY();

            if(chPixel(vertexes, new Pixel((int)mouseEvent.getX(), (int)mouseEvent.getY())))
            {
                pressedOnAllotment = true;
            }
            else
                {
                    if(!vertexes.isEmpty()) cancelAllotment(pixels, startPixels, canvas, 0, 0);

                    SnapshotParameters snapshotParameters = new SnapshotParameters();
                    snapshotParameters.setTransform(Transform.scale(1/canvas.getScaleX(), 1/canvas.getScaleY()));
                    startWritableImage = canvas.snapshot(snapshotParameters, null);

                    pixels.clear();
                    vertexes.clear();
                    vertexes.add(new Pixel((int)pressedX, (int)pressedY));
                }
        }

        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && mousePressed) {

            MouseEvent mouseEvent = (MouseEvent) event;

            if(pressedOnAllotment)
            {
                double deltaX = mouseEvent.getX() - pressedX;
                double deltaY = mouseEvent.getY() - pressedY;

                cancelAllotment(pixels, startPixels, canvas, (int)deltaX, (int)deltaY);

                drawAllotment(pixels, vertexes, deltaX, deltaY, canvas);
            }

            if(!pressedOnAllotment)
            {
                if (pressedX != -1) gc.strokeLine(pressedX, pressedY, mouseEvent.getX(), mouseEvent.getY());
                pressedX = mouseEvent.getX();
                pressedY = mouseEvent.getY();

                vertexes.add(new Pixel((int) mouseEvent.getX(), (int) mouseEvent.getY()));
            }
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
        {
            if(pressedOnAllotment)
            {
                MouseEvent mouseEvent = (MouseEvent) event;

                double deltaX = mouseEvent.getX() - pressedX;
                double deltaY = mouseEvent.getY() - pressedY;

                vertexes.forEach((a) -> a.x += deltaX);
                vertexes.forEach((a) -> a.y += deltaY);

                for(int i = 0; i < pixels.size(); i++)
                {
                    pixels.get(i).x += deltaX;
                    pixels.get(i).y += deltaY;
                }
                pressedOnAllotment = false;
            }
            else
                {
                    gc.strokeLine(vertexes.get(0).x, vertexes.get(0).y, vertexes.get(vertexes.size() - 1).x, vertexes.get(vertexes.size() - 1).y);
                    allocation(vertexes, pixels, startWritableImage);
                    startPixels.clear();
                    pixels.forEach((a) -> startPixels.add(new Pixel(a.x, a.y)));
                }
            mousePressed = false;
            pressedX = -1;
        }

    }

    /**
     * Вызывается при выборе другого инструмента
     * @param canvas холст
     */
    public void detached(EditorCanvas canvas)
    {
        if(!vertexes.isEmpty()) cancelAllotment(pixels, startPixels, canvas, 0, 0);
        pixels.clear();
        vertexes.clear();
        startPixels.clear();
    }

    /**
     * Вызывается при выборе этого инструмента
     * @param canvas холст
     */
    @Override
    public void attached(EditorCanvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);
    }

    /**
     * Определяет пиксели находящиеся в выделении
     * @param vertexes вершины выделения
     * @param pixels пиксели внутри выделения
     * @param snapshotOfCanvas изображение холста
     */
    public void allocation(ArrayList<Pixel> vertexes, ArrayList<Pixel> pixels, WritableImage snapshotOfCanvas)
    {
        PixelReader pixelReader = snapshotOfCanvas.getPixelReader();

        Pixel minPixel = new Pixel(vertexes.get(0).x, vertexes.get(0).y);
        Pixel maxPixel = new Pixel(vertexes.get(0).x, vertexes.get(0).y);

        for(int i = 1; i < vertexes.size(); i++)
        {
            if (vertexes.get(i).x > maxPixel.x) maxPixel.x = vertexes.get(i).x;
            else if (vertexes.get(i).x < minPixel.x) minPixel.x = vertexes.get(i).x;
            if (vertexes.get(i).y > maxPixel.y) maxPixel.y = vertexes.get(i).y;
            else if (vertexes.get(i).y < minPixel.y) minPixel.y = vertexes.get(i).y;
        }

        for(int i = minPixel.x; i < maxPixel.x; i++)
            {
                for(int j = minPixel.y; j < maxPixel.y; j++)
                {
                    if(chPixel(vertexes, new Pixel(i, j))) pixels.add(new Pixel(i, j, pixelReader.getColor(i, j)));
                }
            }
    }

    /**
     * Проверка пикселя на нахождение в многоугольнике
     * @param vertexes вершины многоугольника
     * @param checkPixel пиксель, который проверяем
     * @return
     */
    public boolean chPixel(ArrayList<Pixel> vertexes, Pixel checkPixel)
    {
        boolean result = false;
        int i, j;
        for(i = 0, j = vertexes.size() - 1; i < vertexes.size(); j = i++)
        {
            if (((vertexes.get(i).y > checkPixel.y) != (vertexes.get(j).y > checkPixel.y)) &&
                    (checkPixel.x < (vertexes.get(j).x - vertexes.get(i).x) * (checkPixel.y - vertexes.get(i).y) / (vertexes.get(j).y - vertexes.get(i).y) + vertexes.get(i).x))
            result = !result;
        }
        return result;
    }

    /**
     * Закрашивает белым старое местоположение выделения, и отображает его на новом местоположении
     * @param pixels пиксели выделения
     * @param startPixels начальное место выделения
     * @param canvas холст
     * @param deltaX изменение координат мышки по x
     * @param deltaY изменение координат мышки по y
     */
    public void cancelAllotment(ArrayList<Pixel> pixels, ArrayList<Pixel> startPixels, EditorCanvas canvas, int deltaX, int deltaY){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(startWritableImage, 0, 0);
        PixelWriter pixelWriter = gc.getPixelWriter();
        startPixels.forEach((a) -> pixelWriter.setColor(a.x, a.y, Color.WHITE));

        for(int i = 0; i < pixels.size(); i++)
        {
            if((pixels.get(i).x + deltaX > 0 && pixels.get(i).x + deltaX < canvas.getWidth()) &&
                    (pixels.get(i).y + deltaY > 0 && pixels.get(i).y + deltaY < canvas.getHeight()))
            {
                pixelWriter.setColor(pixels.get(i).x + deltaX, pixels.get(i).y + deltaY, pixels.get(i).color);
            }
        }
    }

    /**
     * Рисует границы выделения
     * @param pixels пиксели выделения
     * @param vertexes вершины выделения
     * @param deltaX изменение координат мышки по x
     * @param deltaY изменение координат мышки по y
     * @param canvas холст
     */
    public void drawAllotment(ArrayList<Pixel> pixels, ArrayList<Pixel> vertexes, double deltaX, double deltaY, EditorCanvas canvas)
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for(int j = 0; j < vertexes.size(); j++)
        {
            if(j != vertexes.size() - 1)gc.strokeLine(vertexes.get(j).x + deltaX, vertexes.get(j).y + deltaY, vertexes.get(j + 1).x + deltaX, vertexes.get(j + 1).y + deltaY);
            else gc.strokeLine(vertexes.get(j).x + deltaX, vertexes.get(j).y + deltaY, vertexes.get(0).x + deltaX, vertexes.get(0).y + deltaY);
        }
    }
}

