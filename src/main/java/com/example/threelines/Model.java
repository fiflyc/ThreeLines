package com.example.threelines;

public class Model {

    private enum State {
        START_MENU,
        ALERT,
        GAME,
        GAME_SELECTED_TILE,
        GAME_SELECTED_DIRECTION
    }

    private final View view;

    private State state;
    private Board board;
    private Board.Tile selectedTile;
    private Board.Field selectedField;

    public Model(View view) {
        this.view = view;
        state = State.START_MENU;
    }

    public void sendCommand(Command command) {
        switch (command) {
            case NEW_GAME:
                startNewGame(Board.Type.CLASSIC);
                break;
            case SOFT_NEW_GAME:
                state = State.ALERT;
                view.showNewGameMenu();
                break;
            case SOFT_EXIT:
                state = State.ALERT;
                view.showExitMenu();
                break;
            case EXIT:
                view.closeApplication();
                break;
            case CONTINUE_GAME:
                state = State.GAME;
                view.showBoardWindow(board);
                view.selectField(selectedField);
                break;
        }
    }

    public void sendCommand(Control control) {
        if (state == State.GAME) {
            handleControlInGameState(control);
        } else if (state == State.GAME_SELECTED_TILE) {
            handleControlInSelectedTileState(control);
        } else if (state == State.GAME_SELECTED_DIRECTION) {
            if (control == Control.SELECT) {
                if (board.canMove(selectedTile, selectedField)) {
                    state = State.GAME;
                    board.move(selectedTile, selectedField);
                    view.moveTile(selectedTile, selectedField);
                    view.unselectTile(selectedTile);
                    selectedTile = null;

                    if (board.isEnd()) {
                        state = State.ALERT;
                        view.showEndMenu();
                    }
                }
            } else {
                handleControlInSelectedTileState(control);
            }
        }
    }

    private void handleControlInGameState(Control control) {
        switch (control) {
            case SELECT:
                if (selectedField.getTile() != null) {
                    state = State.GAME_SELECTED_TILE;
                    selectedTile = selectedField.getTile();
                    view.selectTile(selectedTile);
                }
                break;
            case LEFT:
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedField.getLeft()))? selectedField.getLeft() : selectedField;
                view.selectField(selectedField);
                break;
            case RIGHT:
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedField.getRight()))? selectedField.getRight() : selectedField;
                view.selectField(selectedField);
                break;
            case UP:
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedField.getUp()))? selectedField.getUp() : selectedField;
                view.selectField(selectedField);
                break;
            case DOWN:
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedField.getDown()))? selectedField.getDown() : selectedField;
                view.selectField(selectedField);
                break;
        }
    }

    private boolean canBeChosen(Board.Field field) {
        return field != null && field.getState() != Board.Field.State.BLOCKED;
    }

    private void handleControlInSelectedTileState(Control control) {
        switch (control) {
            case SELECT:
            case UNSELECT:
                state = State.GAME;
                view.unselectTile(selectedTile);
                selectedTile = null;
                break;
            case LEFT:
                state = State.GAME_SELECTED_DIRECTION;
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedTile.getField().getLeft())) ? selectedTile.getField().getLeft() : selectedField;
                view.selectField(selectedField);
                break;
            case RIGHT:
                state = State.GAME_SELECTED_DIRECTION;
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedTile.getField().getRight())) ? selectedTile.getField().getRight() : selectedField;
                view.selectField(selectedField);
                break;
            case UP:
                state = State.GAME_SELECTED_DIRECTION;
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedTile.getField().getUp())) ? selectedTile.getField().getUp() : selectedField;
                view.selectField(selectedField);
                break;
            case DOWN:
                state = State.GAME_SELECTED_DIRECTION;
                view.unselectField(selectedField);
                selectedField = (canBeChosen(selectedTile.getField().getDown())) ? selectedTile.getField().getDown() : selectedField;
                view.selectField(selectedField);
                break;
        }
    }

    private void startNewGame(Board.Type boardType) {
        board = new Board(boardType);
        state = State.GAME;
        selectedField = board.get(0, 0);
        view.showBoardWindow(board);
        view.selectField(selectedField);
    }
}
