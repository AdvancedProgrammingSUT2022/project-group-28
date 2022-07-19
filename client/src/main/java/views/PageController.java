package views;

import javafx.animation.Transition;

import java.util.ArrayList;

public abstract class PageController {
    protected ArrayList<Transition> transitions = new ArrayList<>();

    protected void onExit() {
        for (Transition transition : this.transitions) {
            transition.stop();
        }
    }

}
