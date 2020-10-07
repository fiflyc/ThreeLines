package com.example.threelines;

public interface View {

    void setController(Controller controller);

    void showStartMenu();

    void showEndMenu();

    void showExitMenu();

    void closeApplication();

    void showBoardWindow(Board board);

    void moveTile(Board.Tile tile, Board.Field target);

    void selectTile(Board.Tile tile);

    void selectField(Board.Field field);

    void unselectTile(Board.Tile tile);

    void unselectField(Board.Field field);
}
