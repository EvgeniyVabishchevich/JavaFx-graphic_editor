/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.MenuBar;
import javafx.geometry.HPos;

/**
 *
 * @author evgen
 */
public class JavaFXApplication1 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    
    GridPane TopInterface = new GridPane();
        
    ColumnConstraints [] columnsArray = new ColumnConstraints[15];
    for (int i = 0; i < columnsArray.length; i++){
        columnsArray[i] = new ColumnConstraints();
    }
    
    for (int i = 0; i < columnsArray.length; i++) {
        TopInterface.getColumnConstraints().addAll(columnsArray[i]);
        if(i < 10) columnsArray[i].setPercentWidth(5);
        else columnsArray[i].setPercentWidth(10);
    }
    
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    MenuItem exitMenuItem = new MenuItem("Exit");
    exitMenuItem.setOnAction(actionEvent -> Platform.exit());
    
    fileMenu.getItems().addAll(exitMenuItem);
    menuBar.getMenus().addAll(fileMenu);
    
    Button [] buttonArray = new Button[25];         //11-20 colors, 20-25(copy,paste,cut,back,forward)
    for (int i = 0; i < buttonArray.length; i++){
        buttonArray[i] = new Button();
    }
    
    for(int i = 0; i < 5; i++){                 //Placement of the main instruments
        TopInterface.add(buttonArray[i], i, 1);
        TopInterface.add(buttonArray[i+5], i, 2);
    }
    
    for(int i = 5; i < 10; i++){                //Placement of palette
        TopInterface.add(buttonArray[i+5], i, 1);
        TopInterface.add(buttonArray[i+10], i, 2);
    }
    
    for(int i = 20; i < 25; i++){
        TopInterface.add(buttonArray[i], i - 10, 1);
    }
    
    TopInterface.add(menuBar, 0, 0);
    GridPane.setConstraints(menuBar, 0, 0, 15, 1);
    GridPane.setConstraints(buttonArray[20], 10, 1, 1, 2);//Increasing the size of some functions
    GridPane.setConstraints(buttonArray[21], 11, 1, 1, 2);
    GridPane.setConstraints(buttonArray[22], 12, 1, 1, 2);
    GridPane.setConstraints(buttonArray[23], 13, 1, 1, 2);
    GridPane.setConstraints(buttonArray[24], 14, 1, 1, 2);
    
    Scene mainScene = new Scene(TopInterface, 500, 500);
    primaryStage.setScene(mainScene);
    primaryStage.show();
    
    
    
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
