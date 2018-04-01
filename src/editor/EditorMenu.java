package editor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditorMenu extends MenuBar {

    public EditorMenu(EditorCanvas canvas, Stage stage)
    {
        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem saveMenuItem = new MenuItem("Save");//TODO
        MenuItem exitMenuItem = new MenuItem("Exit");

        /**
         * Создание нового файла и сохранение старого
         */
        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!canvas.isSnapshotsEmpty()) {

                    Stage questionToSaveStage = new Stage();
                    GridPane questionToSaveRoot = new GridPane();

                    questionToSaveRoot.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints());

                    Label toSaveLbl = new Label("Save the current file?");

                    Button yes = new Button("Save");
                    Button no = new Button("Not Save");
                    Button cancel = new Button("Cancel");

                    questionToSaveRoot.add(toSaveLbl, 0, 0, 3, 1);
                    questionToSaveRoot.add(yes, 0, 1);
                    questionToSaveRoot.add(no, 1, 1);
                    questionToSaveRoot.add(cancel, 2, 1);

                    yes.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            canvas.saveImage(stage);
                            newFile(canvas);
                            questionToSaveStage.close();
                        }
                    });

                    no.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            newFile(canvas);
                            questionToSaveStage.close();
                        }
                    });

                    cancel.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            questionToSaveStage.close();
                        }
                    });

                    questionToSaveStage.setScene(new Scene(questionToSaveRoot, questionToSaveRoot.getMinWidth(), questionToSaveRoot.getMinHeight()));
                    questionToSaveStage.show();
                }
                else
                    newFile(canvas);
            }
        });

        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                canvas.openImage(stage);
            }
        });

        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvas.saveImage(stage);
            }
        });

        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newMenuItem,openMenuItem,saveMenuItem,exitMenuItem);

        this.getMenus().addAll(fileMenu);
    }

    /**
     * Создание нового холста
     * @param canvas холст
     */
    private void newFile(EditorCanvas canvas)
    {
        Stage newFileStage = new Stage();
        GridPane newFileRoot = new GridPane();

        newFileRoot.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints());
        Label heightLbl = new Label("Height");
        Label weightLbl = new Label("Weight");
        TextField height = new TextField("0");
        TextField width = new TextField("0");

        Button cancel = new Button("Cancel");
        Button ok = new Button("Ok");

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newFileStage.close();
            }
        });

        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvas.setHeight(Double.parseDouble(height.getText()));
                canvas.setWidth(Double.parseDouble(width.getText()));
                canvas.clear(Color.WHITE);
                newFileStage.close();
            }
        });

        newFileRoot.add(heightLbl, 0, 0);
        newFileRoot.add(weightLbl, 0, 1);
        newFileRoot.add(height, 1,0);
        newFileRoot.add(width, 1,1);
        newFileRoot.add(cancel, 0, 2);
        newFileRoot.add(ok, 1, 2);

        newFileStage.setScene(new Scene(newFileRoot, newFileRoot.getMinWidth(), newFileRoot.getMinHeight()));
        newFileStage.show();
    }
}
