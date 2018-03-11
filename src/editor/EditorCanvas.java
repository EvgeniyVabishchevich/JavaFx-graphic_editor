package editor;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class EditorCanvas extends Canvas {

    final FileChooser fileChooser = new FileChooser();

    private InstrumentsPanel instrumentsPanel;

    private EditorCanvas instance()
    {
        return this;
    }

    public EditorCanvas(int width, int height, InstrumentsPanel instrumentsPanel)
    {
        super(width, height);
        clear(Color.WHITE);

        this.instrumentsPanel = instrumentsPanel;
        this.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                instrumentsPanel.getCurrentInstrument().handleEvent(event, instance());
            }
        });

        this.addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                instrumentsPanel.getCurrentInstrument().handleEvent(keyEvent, instance());
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
     * Функция окраски холста в белый
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

}

