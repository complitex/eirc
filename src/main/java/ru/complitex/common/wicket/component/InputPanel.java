package ru.complitex.common.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Anatoly A. Ivanov
 * 14.09.2018 14:02
 */
public class InputPanel extends Panel {
    public static String INPUT_COMPONENT_ID = "input";

    public InputPanel(String id, Component input) {
        super(id);

        add(input);
    }
}
