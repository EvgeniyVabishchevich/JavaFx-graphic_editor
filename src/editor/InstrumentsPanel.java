package editor;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;

import java.lang.System;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Button [] arrayOfInstrumentsButtons;

    private Color mainColor;

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

        ColorPicker colorPicker = new ColorPicker();
        this.add(colorPicker, 0, arrayOfInstrumentsButtons.length/2 + 1, 2, 1);

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainColor = colorPicker.getValue();
            }
        });

        arrayOfInstrumentsButtons[0].setStyle("-fx-graphic: url(pencil.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[1].setStyle("-fx-graphic: url(fillinstrument.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[2].setStyle("-fx-graphic: url(line.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[3].setStyle("-fx-graphic: url(rectangle.png); -fx-padding: 1px ");
        arrayOfInstrumentsButtons[4].setStyle("-fx-graphic: url(circle.png); -fx-padding: 1px ");

        for(int i = 0; i < arrayOfInstrumentsButtons.length; i++) {
            arrayOfInstrumentsButtons[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentInstrumentIndex = Arrays.asList(arrayOfInstrumentsButtons).indexOf(this);
                    System.out.println(getCurrentInstrument().getClass().getName());
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

    public Color getCurrentMainColor()
    {
        return this.mainColor;
    }

}
