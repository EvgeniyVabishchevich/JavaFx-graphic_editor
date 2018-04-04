package editor;

import javafx.scene.paint.Color;

/**
 * Pixel класс содержащий координаты пикселя
 */
public class Pixel {

    /**
     * Координаты пикселя
     */
    public int x,y;
    public Color color;

    public Pixel(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Pixel(int x, int y, Color color)
    {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public void setCoord(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
