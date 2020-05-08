package ru.complitex.common.ui.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Anatoly A. Ivanov
 * 14.09.2018 14:02
 */
public class InputPanel extends Panel {
    public static String ID = "input";

    public InputPanel(String id, Component input) {
        super(id);

        add(input);
    }

    public static InputPanel of(String id, Component input){
        return new InputPanel(id, input);
    }
}
