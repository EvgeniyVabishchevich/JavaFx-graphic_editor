package editor;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import java.lang.System;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;

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

    private Button [] arrayOfInstrumentsButtons;

    public InstrumentsPanel()
    {
        instruments = new ArrayList<>();
        instruments.add(new PencilInstrument());
        instruments.add(new FillInstrument());

        currentInstrumentIndex = 0;

        Button but = new Button();
        but.setStyle("-fx-graphic: url(pencil.png) ");

        this.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());
        arrayOfInstrumentsButtons = new Button[instruments.size()];
        for(int i = 0; i < arrayOfInstrumentsButtons.length/2; i++)
        {
            arrayOfInstrumentsButtons[i] = new Button();
            arrayOfInstrumentsButtons[i + arrayOfInstrumentsButtons.length/2] = new Button();
            this.add(arrayOfInstrumentsButtons[i], 0, i);
            this.add(arrayOfInstrumentsButtons[i + arrayOfInstrumentsButtons.length/2], 1, i + arrayOfInstrumentsButtons.length/2 - 1);
        }
        this.add(but, 0, 1);


        arrayOfInstrumentsButtons[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(currentInstrumentIndex == instruments.size() - 1)
                {
                    currentInstrumentIndex = 0;
                }
                else currentInstrumentIndex += 1;
                System.out.println(getCurrentInstrument().getClass().getName());
            }
        });

    }


    /**
     * @return текущий выбранный инструмент
     */
    public Instrument getCurrentInstrument()
    {
        return instruments.get(currentInstrumentIndex);
    }


}
