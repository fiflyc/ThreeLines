package com.example.threelines;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        PCView view = new PCView();
        Model model = new Model(view);
        Controller controller = new PCController(model);
        view.setController(controller);
        view.setStage(primaryStage);

        view.showStartMenu();
    }
}
