package com.example.threelines;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

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
