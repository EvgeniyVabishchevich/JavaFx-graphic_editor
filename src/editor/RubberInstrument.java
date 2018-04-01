package editor;

import javafx.scene.paint.Color;

public class RubberInstrument extends PencilInstrument{
    public void setFill(EditorCanvas canvas)
    {
        canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        canvas.getGraphicsContext2D().setLineWidth(canvas.getInstrumentPanel().getCurrentThickness());
    }
}
