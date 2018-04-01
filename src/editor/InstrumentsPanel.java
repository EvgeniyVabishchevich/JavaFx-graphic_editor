package editor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * InstrumentsPanel обрабатывает выбор инструмента.
 */
public class InstrumentsPanel extends GridPane
{
    /**
     *  Список всех инструментов находящихся на панели инструментов
     */
    private List<Instrument> instruments;

    /**
     * Индекс текущего инструмента в списке инструментов
     */
    private int currentInstrumentIndex;

    /**
     * Массив кнопок для инструментов
     */
    private Button [] arrayOfInstrumentsButtons;

    /**
     * Выбранный цвет рисования
     */
    private Color currentMainColor;

    private double currentThickness;

    public InstrumentsPanel()
    {
        instruments = new ArrayList<>();
        instruments.add(new PencilInstrument());
        instruments.add(new FillInstrument());
        instruments.add(new LineInstrument());
        instruments.add(new RectInstrument());
        instruments.add(new OvalInstrument());
        instruments.add(new RubberInstrument());
        instruments.add(new AllotmentInstrument());
        instruments.add(new ScaleInstrument());

        currentInstrumentIndex = 0;

        this.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());
        arrayOfInstrumentsButtons = new Button[instruments.size()];

        for(int i = 0; i < arrayOfInstrumentsButtons.length; i++)
        {
            for(int j = 0; j < 2; j++)
            {
                if(i*2+j >= arrayOfInstrumentsButtons.length) break;
                arrayOfInstrumentsButtons[i*2 + j] = new Button();
                this.add(arrayOfInstrumentsButtons[i*2 + j], j, i);
            }
        }

        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        currentMainColor = colorPicker.getValue();



        this.add(colorPicker, 0, arrayOfInstrumentsButtons.length/2 + 1, 2, 1);
        this.add(choiceBox, 0, arrayOfInstrumentsButtons.length/2 + 2, 2, 1);

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentMainColor = colorPicker.getValue();
            }
        });

        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currentThickness = newValue.doubleValue() + 1;
            }
        });

        /**
         * Добавление изображений инструментов для кнопок
         */
        arrayOfInstrumentsButtons[0].setStyle("-fx-graphic: url(pencil.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[1].setStyle("-fx-graphic: url(fillinstrument.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[2].setStyle("-fx-graphic: url(line.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[3].setStyle("-fx-graphic: url(rectangle.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[4].setStyle("-fx-graphic: url(circle.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[5].setStyle("-fx-graphic: url(rubber.png); -fx-padding: 1px ");


        for(int i = 0; i < arrayOfInstrumentsButtons.length; i++) {
            final int index = i;
            arrayOfInstrumentsButtons[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentInstrumentIndex = index;
                    EditorLog.log(getCurrentInstrument().getClass().getName());
                }
            });
        }
    }


    /**
     * @return текущий выбранный инструмент
     */
    public Instrument getCurrentInstrument()
    {
        return instruments.get(currentInstrumentIndex);
    }

    /**
     * @return текущий цвет рисования
     */
    public Color getCurrentMainColor()
    {
        return currentMainColor;
    }

    public double getCurrentThickness()
    {
        return currentThickness;

    }

}
