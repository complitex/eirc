package ru.complitex.address.component.input;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import ru.complitex.address.entity.Country;
import ru.complitex.address.entity.Region;
import ru.complitex.address.model.AddressModel;
import ru.complitex.common.entity.Filter;
import ru.complitex.domain.component.form.DomainInput;
import ru.complitex.domain.entity.Domain;

/**
 * @author Anatoly Ivanov
 * 11.06.2020 18:36
 */
public class RegionInput extends Panel {
    private final IModel<Long> countryModel;
    private final IModel<Long> regionModel;

    private final DomainInput country;
    private final DomainInput region;

    private boolean regionRequired;

    public RegionInput(String id, IModel<Long> regionModel) {
        super(id);

        setOutputMarkupId(true);

        this.regionModel = regionModel;

        countryModel = new AddressModel(Region.ENTITY, regionModel, Region.COUNTRY);

        country = new DomainInput("country", Country.ENTITY, countryModel, Country.NAME){
            @Override
            protected void onChangeId(AjaxRequestTarget target) {
                updateRegion(target);

                onRegionChange(target);
            }
        };
        country.setPlaceholder(new StringResourceModel("_country", this));

        add(country);

        region = new DomainInput("region", Region.ENTITY, regionModel, Region.NAME){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Region.COUNTRY, countryModel.getObject());
            }

            @Override
            protected void onChangeId(AjaxRequestTarget target) {
                updateCountry(target);

                onRegionChange(target);
            }

            @Override
            public boolean isRequired() {
                return isRegionRequired();
            }
        };
        region.setPlaceholder(new StringResourceModel("_region", this));

        add(region);
    }

    public IModel<Long> getRegionModel() {
        return regionModel;
    }

    public boolean isRegionRequired() {
        return regionRequired;
    }

    public RegionInput setRegionRequired(boolean regionRequired) {
        this.regionRequired = regionRequired;

        return this;
    }

    protected void onRegionChange(AjaxRequestTarget target){

    }

    protected void updateCountry(AjaxRequestTarget target) {
        if (countryModel.getObject() != null) {
            countryModel.setObject(null);

            target.add(country);
        }
    }

    protected void updateRegion(AjaxRequestTarget target) {
        if (regionModel.getObject() != null) {
            regionModel.setObject(null);

            target.add(region);
        }
    }
}
