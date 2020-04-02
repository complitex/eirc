package ru.complitex.eirc.page.login;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Alert;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import ru.complitex.eirc.page.resource.EircCssResourceReference;

/**
 * @author Anatoly A. Ivanov
 * 29.12.2017 19:25
 */
public class LoginPage extends WebPage {
    public LoginPage(PageParameters parameters) {
        setVersioned(false);

        add(new Alert("error", new ResourceModel("error_login"))
                .type(Alert.Type.Danger)
                .setCloseButtonVisible(true)
                .setVisible(!parameters.get("error").isNull()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(EircCssResourceReference.INSTANCE));
    }
}
