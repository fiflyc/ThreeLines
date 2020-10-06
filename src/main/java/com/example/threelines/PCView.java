package com.example.threelines;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class PCView implements View {

    private Controller controller;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void showStartMenu() {
        VBox root = new VBox();

        Button buttonPlay = new Button("Новая игра");
        buttonPlay.setOnAction(event -> controller.onButtonPressed(Command.NEW_GAME));
        Button buttonExit = new Button("Выход");
        buttonExit.setOnAction(event -> controller.onButtonPressed(Command.EXIT));

        root.getChildren().addAll(buttonPlay, buttonExit);

        Scene scene = new Scene(root, 250, 450);
        stage.setTitle("Три линии");
        stage.setScene(scene);

        stage.show();
    }

    public void showEndMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Задача решена");
        alert.getButtonTypes().clear();

        ButtonType newGame = new ButtonType("Новая игра");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().addAll(newGame, exit);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isEmpty()) {
            controller.onButtonPressed(Command.EXIT);
        } else if (option.get() == newGame) {
            controller.onButtonPressed(Command.NEW_GAME);
        } else if (option.get() == exit) {
            controller.onButtonPressed(Command.EXIT);
        }
    }

    public void showExitMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Выход");
        alert.setHeaderText("Вы уверены, что хотите выйти?");
        alert.getButtonTypes().clear();

        ButtonType continueGame = new ButtonType("Продолжить игру");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().addAll(continueGame, exit);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isEmpty()) {
            controller.onButtonPressed(Command.CONTINUE_GAME);
        } else if (option.get() == continueGame) {
            controller.onButtonPressed(Command.CONTINUE_GAME);
        } else if (option.get() == exit) {
            controller.onButtonPressed(Command.EXIT);
        }
    }

    public void closeApplication() {
        Platform.exit();
    }

    public void showBoard(Board board) {

    }

    public void selectTile(Board.Tile tile) {

    }

    public void selectField(Board.Field field) {

    }

    public void unselectTile(Board.Tile tile) {

    }

    public void unselectField(Board.Field field) {

    }
}
