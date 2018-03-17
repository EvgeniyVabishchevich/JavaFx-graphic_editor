package editor;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import java.lang.System;
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

    public InstrumentsPanel()
    {
        instruments = new ArrayList<>();
        instruments.add(new PencilInstrument());
        instruments.add(new FillInstrument());
        instruments.add(new LineInstrument());
        instruments.add(new RectInstrument());
        instruments.add(new OvalInstrument());

        currentInstrumentIndex = 0;

        this.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());
        arrayOfInstrumentsButtons = new Button[instruments.size()];
        for(int i = 0; i < arrayOfInstrumentsButtons.length; i ++)
        {
            arrayOfInstrumentsButtons[i] = new Button();

            if( i <= arrayOfInstrumentsButtons.length/2)
            {
             this.add(arrayOfInstrumentsButtons[i], 0, i);
            }

            if( i > arrayOfInstrumentsButtons.length/2 )
            {
                this.add(arrayOfInstrumentsButtons[i], 1, i - arrayOfInstrumentsButtons.length/2 - 1);
            }
        }

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        currentMainColor = colorPicker.getValue();

        this.add(colorPicker, 0, arrayOfInstrumentsButtons.length/2 + 1, 2, 1);

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currentMainColor = colorPicker.getValue();
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
        return this.currentMainColor;
    }

}
