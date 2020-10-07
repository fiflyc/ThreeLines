package com.example.threelines;

public class PCController implements Controller {

    private final Model model;

    public PCController(Model model) {
        this.model = model;
    }

    public void pressedKey(String key) {
        switch (key.toLowerCase()) {
            case "ф":
            case "a":
                model.sendCommand(Control.LEFT);
                break;
            case "в":
            case "d":
                model.sendCommand(Control.RIGHT);
                break;
            case "ц":
            case "w":
                model.sendCommand(Control.UP);
                break;
            case "ы":
            case "s":
                model.sendCommand(Control.DOWN);
                break;
            case "с":
            case "c":
                model.sendCommand(Control.SELECT);
                break;
            case "у":
            case "e":
                model.sendCommand(Control.UNSELECT);
                break;
        }
    }

    public void onButtonPressed(Command command) {
        model.sendCommand(command);
    }
}
