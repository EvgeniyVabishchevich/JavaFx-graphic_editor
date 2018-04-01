package editor;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphicsEditor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Graphics Editor");
        primaryStage.setScene(new Scene(root, 800, 600));

        InstrumentsPanel instruments = new InstrumentsPanel();
        EditorCanvas canvas = new EditorCanvas(1000, 1000, instruments);
        EditorMenu menu = new EditorMenu(canvas, primaryStage);

        Group group = new Group(canvas);
        ScrollPane scrollPane = new ScrollPane(group);

        root.setTop(menu);
        root.setLeft(instruments);
        root.setCenter(scrollPane);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
