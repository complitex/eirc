package ru.complitex.eirc.page;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.SingleThemeProvider;
import de.agilecoders.wicket.themes.markup.html.bootstrap.BootstrapThemeTheme;
import org.apache.wicket.Page;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.cdi.ConversationPropagation;
import org.apache.wicket.protocol.http.WebApplication;
import ru.complitex.address.page.*;
import ru.complitex.common.application.ServletAuthorizationStrategy;
import ru.complitex.common.application.ServletUnauthorizedListener;
import ru.complitex.company.page.CompanyPage;
import ru.complitex.company.page.matching.CompanyMatchingPage;
import ru.complitex.company.page.sync.CompanySyncPage;
import ru.complitex.eirc.page.login.LoginPage;
import ru.complitex.address.page.matching.*;
import ru.complitex.address.page.sync.*;

/**
 * @author Anatoly A. Ivanov
 * 27.02.2020 9:08 PM
 */
public class EircWebApplication extends WebApplication {

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    @Override
    protected void init() {
        new CdiConfiguration().setPropagation(ConversationPropagation.ALL).configure(this);

        getSecuritySettings().setAuthorizationStrategy(new ServletAuthorizationStrategy());
        getSecuritySettings().setUnauthorizedComponentInstantiationListener(new ServletUnauthorizedListener(LoginPage.class));

        configureBootstrap();
        configureMountPage();

        getCspSettings().blocking().disabled();
    }

    private void configureBootstrap() {
        IBootstrapSettings settings = new BootstrapSettings();
        settings.setThemeProvider(new SingleThemeProvider(new BootstrapThemeTheme()));

        Bootstrap.builder().withBootstrapSettings(settings).install(this);
    }

    private void configureMountPage() {
        mountPage("login", LoginPage.class);

        mountPage("companies", CompanyPage.class);
        mountPage("counties", CountryPage.class);
        mountPage("regions", RegionPage.class);
        mountPage("city-types", CityTypePage.class);
        mountPage("cities", CityPage.class);
        mountPage("districts", DistrictPage.class);
        mountPage("street-types", StreetTypePage.class);
        mountPage("streets", StreetPage.class);
        mountPage("buildings", BuildingPage.class);
        mountPage("apartments", ApartmentPage.class);

        mountPage("matching/companies", CompanyMatchingPage.class);
        mountPage("matching/counties", CountryMatchingPage.class);
        mountPage("matching/regions", RegionMatchingPage.class);
        mountPage("matching/city-types", CityTypeMatchingPage.class);
        mountPage("matching/cities", CityMatchingPage.class);
        mountPage("matching/districts", DistrictMatchingPage.class);
        mountPage("matching/street-types", StreetTypeMatchingPage.class);
        mountPage("matching/streets", StreetMatchingPage.class);
        mountPage("matching/buildings", BuildingMatchingPage.class);
        mountPage("matching/apartments", ApartmentMatchingPage.class);

        mountPage("sync/company", CompanySyncPage.class);
        mountPage("sync/counties", CountrySyncPage.class);
        mountPage("sync/regions", RegionSyncPage.class);
        mountPage("sync/city-types", CityTypeSyncPage.class);
        mountPage("sync/cities", CitySyncPage.class);
        mountPage("sync/districts", DistrictSyncPage.class);
        mountPage("sync/street-types", StreetTypeSyncPage.class);
        mountPage("sync/streets", StreetSyncPage.class);
        mountPage("sync/buildings", BuildingSyncPage.class);
    }
}
