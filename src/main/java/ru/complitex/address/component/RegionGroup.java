package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 11.06.2020 18:36
 */
public class RegionGroup extends Panel {
    @Inject
    private DomainService domainService;

    private final DomainGroup country;
    private final DomainGroup region;

    public RegionGroup(String id, IModel<Long> regionModel, boolean required) {
        super(id);

        setOutputMarkupId(true);

        IModel<Long> countryModel = new IModel<>(){
            private Long countryId;

            @Override
            public Long getObject() {
                if (countryId == null && regionModel.getObject() != null){
                    countryId = domainService.getNumber(Region.ENTITY_NAME, regionModel.getObject(), Region.COUNTRY);
                }

                return countryId;
            }

            @Override
            public void setObject(Long object) {
                countryId = object;
            }
        };

        country = new DomainGroup("country", Country.ENTITY_NAME, Country.NAME, countryModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                regionModel.setObject(null);

                target.add(region);

                RegionGroup.this.onChange(target);
            }
        };
        add(country);

        region = new DomainGroup("region", Region.ENTITY_NAME, Region.NAME, regionModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Region.COUNTRY, countryModel.getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                countryModel.setObject(null);

                target.add(country);

                RegionGroup.this.onChange(target);
            }
        };
        add(region);
    }

    protected void onChange(AjaxRequestTarget target){

    }
}
