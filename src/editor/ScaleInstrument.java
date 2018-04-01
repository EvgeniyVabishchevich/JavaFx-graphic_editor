package editor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

public class ScaleInstrument implements Instrument {

    private final double MaxScale = 16,MinScale = 1;

    @Override
    public <T extends InputEvent> void handleEvent(T event, EditorCanvas canvas) {

        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {

            MouseEvent mouseEvent = (MouseEvent) event;

            if (mouseEvent.isPrimaryButtonDown())
            {
                if (canvas.getScaleX() != MaxScale)
                {
                    canvas.setScaleX(canvas.getScaleX() * 2);
                    canvas.setScaleY(canvas.getScaleY() * 2);
                }
            }

            if (mouseEvent.isSecondaryButtonDown())
            {
                if (canvas.getScaleX() != MinScale)
                {
                    canvas.setScaleX(canvas.getScaleX() / 2);
                    canvas.setScaleY(canvas.getScaleY() / 2);
                }
            }
        }
    }
}
