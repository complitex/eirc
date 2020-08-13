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
import org.apache.wicket.markup.html.link.Link;
import ru.complitex.address.page.*;
import ru.complitex.company.page.CompanyPage;
import ru.complitex.eirc.page.resource.EircCssResourceReference;
import ru.complitex.eirc.page.resource.MenuCssResourceReference;
import ru.complitex.eirc.page.resource.MenuJsResourceReference;
import ru.complitex.eirc.security.EircRoles;
import ru.complitex.address.page.matching.*;
import ru.complitex.address.page.sync.*;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 11:21 PM
 */
@AuthorizeInstantiation(EircRoles.AUTHORIZED)
public class BasePage extends WebPage {
    public BasePage() {
        add(new BookmarkablePageLink<>("home", HomePage.class));

        add(new Link<>("logout") {
            @Override
            public void onClick() {
                BasePage.this.getSession().invalidate();
            }
        });

        WebMarkupContainer address = new WebMarkupContainer("domains");
        add(address);

        address.add(new BookmarkablePageLink<>("companies", CompanyPage.class));
        address.add(new BookmarkablePageLink<>("counties", CountryPage.class));
        address.add(new BookmarkablePageLink<>("regions", RegionPage.class));
        address.add(new BookmarkablePageLink<>("cityTypes", CityTypePage.class));
        address.add(new BookmarkablePageLink<>("cities", CityPage.class));
        address.add(new BookmarkablePageLink<>("districts", DistrictPage.class));
        address.add(new BookmarkablePageLink<>("streetTypes", StreetTypePage.class));
        address.add(new BookmarkablePageLink<>("streets", StreetPage.class));
        address.add(new BookmarkablePageLink<>("buildings", BuildingPage.class));

        WebMarkupContainer matching = new WebMarkupContainer("matching");
        add(matching);

        matching.add(new BookmarkablePageLink<>("counties", CountryMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("regions", RegionMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("cityTypes", CityTypeMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("cities", CityMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("districts", DistrictMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("streetTypes", StreetTypeMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("streets", StreetMatchingPage.class));
        matching.add(new BookmarkablePageLink<>("buildings", BuildingMatchingPage.class));

        WebMarkupContainer sync = new WebMarkupContainer("sync");
        add(sync);

        sync.add(new BookmarkablePageLink<>("counties", CountrySyncPage.class));
        sync.add(new BookmarkablePageLink<>("regions", RegionSyncPage.class));
        sync.add(new BookmarkablePageLink<>("cityTypes", CityTypeSyncPage.class));
        sync.add(new BookmarkablePageLink<>("cities", CitySyncPage.class));
        sync.add(new BookmarkablePageLink<>("districts", DistrictSyncPage.class));
        sync.add(new BookmarkablePageLink<>("streetTypes", StreetTypeSyncPage.class));
        sync.add(new BookmarkablePageLink<>("streets", StreetSyncPage.class));
        sync.add(new BookmarkablePageLink<>("buildings", BuildingSyncPage.class));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(CssHeaderItem.forReference(EircCssResourceReference.INSTANCE));

        response.render(CssHeaderItem.forReference(MenuCssResourceReference.INSTANCE));
        response.render(JavaScriptHeaderItem.forReference(MenuJsResourceReference.INSTANCE));

        response.render(CssHeaderItem.forReference(FontAwesome5CssReference.instance()));

        response.render(OnDomReadyHeaderItem.forScript("$('#menu').metisMenu({toggle: false})"));
    }
}
