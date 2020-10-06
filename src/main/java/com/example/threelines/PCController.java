package com.example.threelines;

public class PCController implements Controller {

    private Model model;

    public PCController(Model model) {
        this.model = model;
    }

    public void pressedKey(String key) {
        System.out.println("Our daddy told us do not been ashamed about our " + key);
    }

    public void onButtonPressed(Command command) {
        model.sendCommand(command);
    }
}
