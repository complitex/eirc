package ru.complitex.sync.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import ru.complitex.domain.entity.Domain;
import ru.complitex.eirc.page.BasePage;

/**
 * @author Anatoly Ivanov
 * 29.04.2020 23:53
 */
public class SyncPage<T extends Domain<T>> extends BasePage {
    public SyncPage(Class<T> domainClass) {
        WebMarkupContainer container = new WebMarkupContainer("container");
        container.setOutputMarkupId(true);
        add(container);

        NotificationPanel feedback = new NotificationPanel("feedback");
        feedback.setOutputMarkupId(true);
        container.add(feedback);


    }
}
