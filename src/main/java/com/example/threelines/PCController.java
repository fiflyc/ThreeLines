package com.example.threelines;

public class PCController implements Controller {

    private Model model;

    public PCController(Model model) {
        this.model = model;
    }

    public void pressedKey(String key) {
        if (key.toLowerCase().equals("a")) {
            model.sendCommand(Control.LEFT);
        } else if (key.toLowerCase().equals("d")) {
            model.sendCommand(Control.RIGHT);
        } else if (key.toLowerCase().equals("w")) {
            model.sendCommand(Control.UP);
        } else if (key.toLowerCase().equals("s")) {
            model.sendCommand(Control.DOWN);
        } else if (key.toLowerCase().equals("c")) {
            model.sendCommand(Control.SELECT);
        } else if (key.toLowerCase().equals("e")) {
            model.sendCommand(Control.UNSELECT);
        }
    }

    public void onButtonPressed(Command command) {
        model.sendCommand(command);
    }
}
