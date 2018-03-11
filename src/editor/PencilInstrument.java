package editor;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PencilInstrument implements Instrument {

    private int pencilThickness;

    PencilInstrument()
    {
        pencilThickness = 3;
    }
    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas)
    {
            GraphicsContext gc = canvas.getGraphicsContext2D();

            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED)
            {
                MouseEvent mouseEvent = (MouseEvent) event;
                gc.fillOval(mouseEvent.getX(), mouseEvent.getY(), pencilThickness, pencilThickness);
            }
            if (event.getEventType() == KeyEvent.KEY_PRESSED)
            {
                KeyEvent keyEvent = (KeyEvent) event;
                System.out.println("Key event");
            }
    }
}
