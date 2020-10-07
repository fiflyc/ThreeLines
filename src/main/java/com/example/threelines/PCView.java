package com.example.threelines;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PCView implements View {

    private static class TileDrawer {

        private Shape shape;

        public TileDrawer(Board.Tile.Type type, double x, double y, double r) {
            switch (type) {
                case RED:
                    shape = new Rectangle(x - r, y - r, 2 * r, 2 * r);
                    shape.setFill(Color.DARKRED);
                    break;
                case ORANGE:
                    shape = new Circle(x, y, r);
                    shape.setFill(Color.ORANGE);
                    break;
                case YELLOW:
                    Polygon poly = new Polygon();
                    poly.getPoints().addAll(
                            x, y - r,
                            x - r, y + r,
                            x + r, y + r
                    );
                    poly.setFill(Color.GOLD);
                    shape = poly;
                    break;
            }
        }

        public void draw(Group group) {
            group.getChildren().add(shape);
        }

        public void moveTo(double x, double y) {
            if (shape instanceof Rectangle) {
                ((Rectangle) shape).setX(x - ((Rectangle) shape).getWidth() / 2);
                ((Rectangle) shape).setY(y - ((Rectangle) shape).getHeight() / 2);
            } else if (shape instanceof Circle) {
                ((Circle) shape).setCenterX(x);
                ((Circle) shape).setCenterY(y);
            } else if (shape instanceof Polygon) {
                double l = ((Polygon) shape).getPoints().get(4) - ((Polygon) shape).getPoints().get(2);
                double r = l / 2;
                ((Polygon) shape).getPoints().clear();
                ((Polygon) shape).getPoints().addAll(
                        x, y - r,
                        x - r, y + r,
                        x + r, y + r
                );
            }
        }

        public void changeColor(Color color) {
            shape.setFill(color);
        }
    }

    private static class FieldDrawer {

        private final double x;
        private final double y;
        private final Rectangle shape;

        public FieldDrawer(Board.Field.State state, double x, double y, double r) {
            this.x = x;
            this.y = y;

            shape = new Rectangle(x - r, y - r, 2 * r, 2 * r);
            if (state == Board.Field.State.BLOCKED) {
                shape.setFill(Color.WHITESMOKE);
            } else {
                shape.setFill(Color.GRAY);
            }
        }

        public void draw(Group group) {
            group.getChildren().add(shape);
        }

        public void changeColor(Color color) {
            shape.setFill(color);
        }
    }

    private Controller controller;
    private Stage stage;
    private final Map<Board.Tile, TileDrawer> tileDrawers = new HashMap<>();
    private final Map<Board.Field, FieldDrawer> fieldDrawers = new HashMap<>();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void showStartMenu() {
        VBox root = new VBox();
        root.setPadding(new Insets(25, 25, 25, 25));

        Button buttonPlay = new Button("Новая игра");
        buttonPlay.setMinSize(200, 50);
        buttonPlay.setOnAction(event -> controller.onButtonPressed(Command.NEW_GAME));

        Button buttonExit = new Button("Выход");
        buttonExit.setMinSize(200, 50);
        buttonExit.setOnAction(event -> controller.onButtonPressed(Command.EXIT));

        Pane spacer = new Pane();
        spacer.setMinSize(200, 50);

        root.getChildren().addAll(buttonPlay, spacer, buttonExit);

        Scene scene = new Scene(root, 250, 200);
        stage.setTitle("Три линии");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public void showEndMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Задача решена");
        alert.getButtonTypes().clear();
        alert.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> controller.onButtonPressed(Command.CONTINUE_GAME));

        ButtonType newGame = new ButtonType("Новая игра");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().addAll(newGame, exit);

        Optional<ButtonType> option = alert.showAndWait();

        if (option.isEmpty()) {
            controller.onButtonPressed(Command.CONTINUE_GAME);
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
        alert.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> controller.onButtonPressed(Command.CONTINUE_GAME));

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

    public void showBoardWindow(Board board) {
        stage.setResizable(false);
        stage.setMinHeight(300 + 100 * board.height);
        stage.setMinWidth(100 + 100 * board.width);

        VBox root = new VBox();
        root.setPadding(new Insets(50, 50, 50, 50));
        Group group = new Group();

        showBoard(board, group);

        Pane vSpacer = new Pane();
        vSpacer.setMinSize(100, 50);

        HBox hbox = new HBox();
        Button newGame = new Button("Новая игра");
        newGame.setFocusTraversable(false);
        newGame.setMinSize(150, 50);
        newGame.setOnAction(event -> controller.onButtonPressed(Command.NEW_GAME));

        Pane hSpacer = new Pane();
        hSpacer.setMinSize(100 * board.width - 300, 50);

        Button buttonExit = new Button("Выход");
        buttonExit.setFocusTraversable(false);
        buttonExit.setMinSize(150, 50);
        buttonExit.setOnAction(event -> controller.onButtonPressed(Command.SOFT_EXIT));

        hbox.getChildren().addAll(newGame, hSpacer, buttonExit);
        root.getChildren().addAll(group, vSpacer, hbox);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> controller.pressedKey(event.getText()));
        stage.setScene(scene);
        stage.show();
    }

    private void showBoard(Board board, Group group) {
        TileDrawer[] columnMarks = new TileDrawer[board.width];
        for (var tileType: Board.Tile.Type.values()) {
            int column = board.type.getTargetColumn(tileType);
            columnMarks[column] = new TileDrawer(tileType, 100 + 100 * column, 75, 25);
            columnMarks[column].changeColor(Color.GRAY);
            columnMarks[column].draw(group);
        }

        tileDrawers.clear();
        fieldDrawers.clear();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                var field = board.get(x, y);
                var fieldDrawer = new FieldDrawer(field.getState(), 100 + 100 * x, 200 + 100 * y, 46);
                fieldDrawers.put(field, fieldDrawer);

                if (field.getState() == Board.Field.State.FILLED) {
                    var tile = field.getTile();
                    var tileDrawer = new TileDrawer(tile.type, 100 + 100 * x, 200 + 100 * y, 35);
                    tileDrawers.put(tile, tileDrawer);
                }
            }
        }
        for (var drawer: fieldDrawers.values()) {
            drawer.draw(group);
        }
        for (var drawer: tileDrawers.values()) {
            drawer.draw(group);
        }
    }

    public void moveTile(Board.Tile tile, Board.Field target) {
        tileDrawers.get(tile).moveTo(fieldDrawers.get(target).x, fieldDrawers.get(target).y);
    }

    public void selectTile(Board.Tile tile) {
        tileDrawers.get(tile).changeColor(Color.DARKSLATEGREY);
    }

    public void selectField(Board.Field field) {
        fieldDrawers.get(field).changeColor(Color.PALEGREEN);
    }

    public void unselectTile(Board.Tile tile) {
        switch (tile.type) {
            case RED:
                tileDrawers.get(tile).changeColor(Color.DARKRED);
                break;
            case ORANGE:
                tileDrawers.get(tile).changeColor(Color.ORANGE);
                break;
            case YELLOW:
                tileDrawers.get(tile).changeColor(Color.GOLD);
                break;
        }
    }

    public void unselectField(Board.Field field) {
        if (field.getState() == Board.Field.State.BLOCKED) {
            fieldDrawers.get(field).changeColor(Color.WHITESMOKE);
        } else {
            fieldDrawers.get(field).changeColor(Color.GRAY);
        }
    }
}
