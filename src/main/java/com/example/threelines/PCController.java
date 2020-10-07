package com.example.threelines;

public class PCController implements Controller {

    private final Model model;

    public PCController(Model model) {
        this.model = model;
    }

    public void pressedKey(String key) {
        switch (key.toLowerCase()) {
            case "a":
                model.sendCommand(Control.LEFT);
                break;
            case "d":
                model.sendCommand(Control.RIGHT);
                break;
            case "w":
                model.sendCommand(Control.UP);
                break;
            case "s":
                model.sendCommand(Control.DOWN);
                break;
            case "c":
                model.sendCommand(Control.SELECT);
                break;
            case "e":
                model.sendCommand(Control.UNSELECT);
                break;
        }
    }

    public void onButtonPressed(Command command) {
        model.sendCommand(command);
    }
}
