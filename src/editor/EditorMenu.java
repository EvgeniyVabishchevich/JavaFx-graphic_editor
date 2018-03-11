package editor;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class EditorMenu extends MenuBar {

    public EditorMenu(EditorCanvas canvas, Stage stage)
    {
        Menu fileMenu = new Menu("File");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem exitMenuItem = new MenuItem("Exit");
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                canvas.openImage(stage);
            }
        });

        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { canvas.saveImage(stage); }
        });

        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(openMenuItem,saveMenuItem,exitMenuItem);

        this.getMenus().addAll(fileMenu);
    }
}
