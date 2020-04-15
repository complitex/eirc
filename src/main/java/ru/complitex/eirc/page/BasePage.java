package ru.complitex.eirc.page;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5CssReference;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import ru.complitex.eirc.page.address.*;
import ru.complitex.eirc.page.resource.EircCssResourceReference;
import ru.complitex.eirc.page.resource.MenuCssResourceReference;
import ru.complitex.eirc.page.resource.MenuJsResourceReference;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:21 PM
 */
@AuthorizeInstantiation(EircRoles.AUTHORIZED)
public class BasePage extends WebPage {
    public BasePage() {
        add(new BookmarkablePageLink<>("brand", HomePage.class));

        WebMarkupContainer address = new WebMarkupContainer("address");
        add(address);

        address.add(new BookmarkablePageLink<>("counties", CountryListPage.class));
        address.add(new BookmarkablePageLink<>("regions", RegionListPage.class));
        address.add(new BookmarkablePageLink<>("cityTypes", CityTypeListPage.class));
        address.add(new BookmarkablePageLink<>("cities", CityListPage.class));
        address.add(new BookmarkablePageLink<>("districts", DistrictListPage.class));
        address.add(new BookmarkablePageLink<>("streetTypes", StreetTypeListPage.class));
        address.add(new BookmarkablePageLink<>("streets", StreetListPage.class));
        address.add(new BookmarkablePageLink<>("buildings", BuildingListPage.class));
        address.add(new BookmarkablePageLink<>("apartments", HomePage.class));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(EircCssResourceReference.INSTANCE));

        response.render(CssHeaderItem.forReference(MenuCssResourceReference.INSTANCE));
        response.render(JavaScriptHeaderItem.forReference(MenuJsResourceReference.INSTANCE));

        response.render(CssHeaderItem.forReference(FontAwesome5CssReference.instance()));

        response.render(OnDomReadyHeaderItem.forScript("$('#menu').metisMenu({toggle: true})"));
    }
}
