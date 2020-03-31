package ru.complitex.eirc.page;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import ru.complitex.eirc.security.EircRoles;

/**
 * @author Anatoly A. Ivanov
 * 27.02.2020 9:12 PM
 */
@AuthorizeInstantiation(EircRoles.AUTHORIZED)
public class HomePage extends BasePage {
}
