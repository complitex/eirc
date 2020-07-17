package ru.complitex.matching.page.address;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import ru.complitex.address.entity.Country;
import ru.complitex.common.component.table.KeyColumn;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.service.DomainService;
import ru.complitex.matching.entity.Matching;
import ru.complitex.matching.page.MatchingPage;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 14.05.2020 18:02
 */
public class CountryMatchingPage extends MatchingPage<Country> {
    @Inject
    private DomainService domainService;

    public CountryMatchingPage() {
        super(Country.class);
    }

    @Override
    protected KeyColumn<Matching> newObjectId(String columnKey) {
        return new KeyColumn<>(columnKey){
            @Override
            protected IModel<?> newItemModel(IModel<Matching> rowModel) {
                return Model.of(domainService.getDomain(Country.class, rowModel.getObject().getObjectId()).getName());
            }
        };
    }

    @Override
    protected Component newObjectId(String componentId, IModel<Matching> model) {
        return new DomainGroup(componentId, Country.ENTITY_NAME, Country.NAME, PropertyModel.of(model, "objectId"))
                .setRequired(true);
    }

    @Override
    protected boolean isParentIdVisible() {
        return false;
    }

    @Override
    protected boolean isAdditionalParentIdVisible() {
        return false;
    }
}
