package editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class EditorCanvas extends Canvas {

    final FileChooser fileChooser = new FileChooser();

    private InstrumentsPanel instrumentsPanel;

    private EditorCanvas instance()
    {
        return this;
    }

    /**
     * Список содержащий скрины холста, пополняющийся после каждого действия любого инструмента
     */
    private LinkedList<WritableImage> snapshots = new LinkedList();

    /**
     * Комбинация клавиш для функции 'назад'
     */
    private static final KeyCombination back = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);

    public EditorCanvas(int width, int height, InstrumentsPanel instrumentsPanel)
    {
        super(width, height);

        clear(Color.WHITE);

        this.instrumentsPanel = instrumentsPanel;
        this.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                requestFocus();
                instrumentsPanel.getCurrentInstrument().handleEvent(event, instance());
            }
        });

        this.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                instrumentsPanel.getCurrentInstrument().handleEvent(keyEvent, instance());
            }
        });

        this.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(back.match(event))
                {
                    setSnapshot(getThis());
                }
            }
        });

        fileChooser.getExtensionFilters().addAll
                (
                        new FileChooser.ExtensionFilter("All", "*.png", "*.jpg", "*.gif"),
                        new FileChooser.ExtensionFilter("*.png", "*.png"),
                        new FileChooser.ExtensionFilter("*.jpg", "*.jpg"),
                        new FileChooser.ExtensionFilter("*.gif", "*.gif")
                );
        /**
         * @TODO разобраться с  setFocusTraversable и  requestFocus
         */
        setFocusTraversable(true);
    }

    /**
     * Открытие файла на жёстком диске
     * @param stage сцена к которой привязывается  FileChooser
     */
    public void openImage(Stage stage)
    {
        try
        {
            File file;
            String filePath = "";
            file = fileChooser.showOpenDialog(stage);
            filePath = file.toURI().toURL().toString();
            Image img = new Image(filePath);
            this.getGraphicsContext2D().drawImage(img, 0, 0);
        }
        catch(java.net.MalformedURLException ne)
        {

        }
        catch(NullPointerException ex)
        {
            System.out.println("File is missing");
        }
    }

    /**
     * Сохранение файла на жёстком диске
     * @param stage сцена к которой привязывается FileChooser
     */
    public void saveImage(Stage stage){
        try
        {
            File outputFile = fileChooser.showSaveDialog(stage);
            WritableImage writableImage = this.snapshot(new SnapshotParameters(), null);
            BufferedImage bImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(bImage, "png", outputFile);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException ex)
        {

        }
    }

    /**
     * Функция окраски холста
     */
    public void clear(Color color)
    {
        for(int i = 0; i < this.getWidth(); i++)
        {
            for(int j = 0; j < this.getHeight(); j++){
                this.getGraphicsContext2D().getPixelWriter().setColor(i, j, color);
            }
        }
    }

    public InstrumentsPanel getInstrumentPanel()
    {
        return this.instrumentsPanel;
    }

    /**
     * Добавляет скрин холста в список snapshots
     * @param canvas холст
     */
    public void getSnapshot(EditorCanvas canvas)
    {
        snapshots.add(canvas.snapshot(new SnapshotParameters(), null));
        System.out.println("Added");
    }

    /**
     * Достает и удаляет последнее изобажение из списка,рисуя его на холст
     * @param canvas холст
     */
    public void setSnapshot(EditorCanvas canvas)
    {
        canvas.getGraphicsContext2D().drawImage(snapshots.pollLast(), 0, 0);
        System.out.println("Removed");
    }

    public EditorCanvas getThis()
    {
        return this;
    }

}
