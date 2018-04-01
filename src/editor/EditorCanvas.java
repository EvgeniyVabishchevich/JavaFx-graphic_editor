package editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import java.util.Stack;


public class EditorCanvas extends Canvas {

    /**
     * Сохраняет и открывает файлы
     */
    final private FileChooser fileChooser = new FileChooser();

    /**
     * Обрабатывает выбор инструмента
     */
    private InstrumentsPanel instrumentsPanel;

    /**
     * Скрины состояний холста, используются для отмены действий в редакторе
     */
    private Stack<WritableImage> snapshots = new Stack<>();

    /**
     * Максимальный зум
     */
    final double MAXSCALE = 4;

    /**
     * Минимальный зум
     */
    final double MINSCALE = 0.25;

    /**
     * EdidorCanvas холст
     * @param width ширина
     * @param height высота
     * @param instrumentsPanel
     */
    public EditorCanvas(int width, int height, InstrumentsPanel instrumentsPanel)
    {
        super(width, height);

        clear(Color.WHITE);

        this.instrumentsPanel = instrumentsPanel;

        this.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                requestFocus();
                instrumentsPanel.getCurrentInstrument().handleEvent(event, EditorCanvas.this);
            }
        });

        this.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.Z && keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                {
                    undoAction();
                }
                if (keyEvent.isControlDown() )
                instrumentsPanel.getCurrentInstrument().handleEvent(keyEvent, EditorCanvas.this);
            }
        });

        this.addEventHandler(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if(event.isControlDown()){
                    if(event.getDeltaY() > 0)
                    {
                        if(getScaleX() != MAXSCALE)
                        {
                            setScaleX(getScaleX() * 2);
                            setScaleY(getScaleY() * 2);
                        }
                    }
                    else
                        {
                            if(getScaleX() != MINSCALE)
                            {
                                setScaleX(getScaleX() / 2);
                                setScaleY(getScaleY() / 2);
                            }
                        }
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
     * Функция окраски холста заданным цветом
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

    /**
     * @return Панель инструментов
     */
    public InstrumentsPanel getInstrumentPanel()
    {
        return this.instrumentsPanel;
    }

    /**
     * Добавляет скрин холста в список snapshots
     */
    public void addSnapshot(WritableImage startWritableImage)
    {
        snapshots.push(startWritableImage);
    }

    /**
     * Отменяет действие на холсте
     */
    public void undoAction()
    {
        if(!isSnapshotsEmpty()) {
            getGraphicsContext2D().drawImage(snapshots.pop(), 0, 0);
        }
    }

    public boolean isSnapshotsEmpty()
    {
        return (snapshots.empty()) ? true : false;
    }
}

