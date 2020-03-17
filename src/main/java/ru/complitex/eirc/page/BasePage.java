package ru.complitex.eirc.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:21 PM
 */
@AuthorizeInstantiation(EircRoles.AUTHORIZED)
public class BasePage extends WebPage {
    public BasePage() {
        WebMarkupContainer address = new WebMarkupContainer("address");
        add(address);

//        address.add(new BookmarkablePageLink("counties", ))




    }
}
