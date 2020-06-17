package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.component.model.AddressModel;
import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 11.06.2020 18:36
 */
public class RegionGroup extends Panel {
    private final IModel<Long> countryModel;
    private final IModel<Long> regionModel;

    private final DomainGroup country;
    private final DomainGroup region;

    public RegionGroup(String id, IModel<Long> regionModel, boolean required) {
        super(id);

        this.regionModel = regionModel;

        setOutputMarkupId(true);

        countryModel = new AddressModel(Region.ENTITY_NAME, regionModel, Region.COUNTRY);

        country = new DomainGroup("country", Country.ENTITY_NAME, Country.NAME, countryModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                if (regionModel.getObject() != null) {
                    regionModel.setObject(null);

                    target.add(region);
                }

                onRegionChange(target);
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
                if (countryModel.getObject() != null) {
                    countryModel.setObject(null);

                    target.add(country);
                }

                onRegionChange(target);
            }
        };
        add(region);
    }

    public IModel<Long> getRegionModel() {
        return regionModel;
    }

    protected void onRegionChange(AjaxRequestTarget target){

    }

    protected void clear(AjaxRequestTarget target){
        if (countryModel.getObject() != null) {
            countryModel.setObject(null);

            target.add(country);
        }

        if (regionModel.getObject() != null) {
            regionModel.setObject(null);

            target.add(region);
        }
    }
}
