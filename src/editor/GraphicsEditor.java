package editor;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GraphicsEditor extends Application {

    /**
     * Окно для панели выбора цвета
     */
    private Stage colorPickerStage;

    /**
     * Сцена для панели выбора цвета
     */
    private Scene colorPickerScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Graphics Editor");
        primaryStage.setScene(new Scene(root, 800, 600));


        ColorPicker colorPicker = new ColorPicker();
        colorPickerScene = new Scene(colorPicker, 200, 50);
        colorPickerStage = new Stage();
        colorPickerStage.setScene(colorPickerScene);
        colorPickerStage.setTitle("Color Picker");


        InstrumentsPanel instruments = new InstrumentsPanel();
        EditorCanvas canvas = new EditorCanvas(400, 400, instruments);
        EditorMenu menu = new EditorMenu(canvas, primaryStage);

        root.setTop(menu);
        root.setCenter(canvas);
        root.setLeft(instruments);

        primaryStage.show();
        colorPickerStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
