package ru.complitex.common.component.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Anatoly Ivanov
 * 28.07.2020 23:15
 */
public abstract class AjaxLinkPanel extends Panel {
    public AjaxLinkPanel(String id, String iconClass) {
        super(id);

        AjaxLink<?> link = new AjaxLink<>("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                AjaxLinkPanel.this.onClick(target);
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.setEventPropagation(AjaxRequestAttributes.EventPropagation.STOP);
            }
        };

        add(link);

        link.add(new WebMarkupContainer("icon"){
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);

                if (iconClass != null){
                    tag.put("class", iconClass);
                }
            }

            @Override
            public boolean isVisible() {
                return iconClass != null;
            }
        });
    }

    public abstract void onClick(AjaxRequestTarget target);
}
