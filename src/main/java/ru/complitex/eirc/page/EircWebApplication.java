package ru.complitex.eirc.page;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author Anatoly A. Ivanov
 * 27.02.2020 9:08 PM
 */
public class EircWebApplication extends WebApplication {
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }
}
