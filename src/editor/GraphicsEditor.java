package editor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphicsEditor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Graphics Editor");
        primaryStage.setScene(new Scene(root, 800, 600));

        InstrumentsPanel instruments = new InstrumentsPanel();
        EditorCanvas canvas = new EditorCanvas(500, 500, instruments);
        EditorMenu menu = new EditorMenu(canvas, primaryStage);

        root.setTop(menu);
        root.setCenter(canvas);
        root.setLeft(instruments);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
