package uk.ac.rhul.cs2810;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    
    
    BorderPane root = new BorderPane();
    Scene scene = new Scene(root, 400, 400);
    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
