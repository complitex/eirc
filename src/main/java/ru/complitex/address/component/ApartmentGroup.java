package ru.complitex.address.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import ru.complitex.address.entity.Apartment;
import ru.complitex.common.entity.Filter;
import ru.complitex.common.model.LoadableModel;
import ru.complitex.domain.component.form.DomainGroup;
import ru.complitex.domain.entity.Domain;
import ru.complitex.domain.service.DomainService;

import javax.inject.Inject;

/**
 * @author Anatoly Ivanov
 * 17.06.2020 17:20
 */
public class ApartmentGroup extends Panel {
    @Inject
    private DomainService domainService;

    private final BuildingGroup building;
    private final DomainGroup apartment;

    public ApartmentGroup(String id, IModel<Long> apartmentModel, boolean required) {
        super(id);

        setOutputMarkupId(true);

        IModel<Long> buildingModel = LoadableModel.of(() -> domainService.getNumber(Apartment.ENTITY_NAME,
                apartmentModel.getObject(), Apartment.BUILDING));

        building = new BuildingGroup("building", buildingModel, false){
            @Override
            protected void onChange(AjaxRequestTarget target) {
                apartmentModel.setObject(null);

                target.add(apartment);

                ApartmentGroup.this.onChange(target);
            }
        };
        add(building);

        apartment = new DomainGroup("apartment", Apartment.ENTITY_NAME, Apartment.NAME, apartmentModel, required){
            @Override
            protected void onFilter(Filter<Domain<?>> filter) {
                filter.getObject().setNumber(Apartment.BUILDING, buildingModel.getObject());
            }

            @Override
            protected void onChange(AjaxRequestTarget target) {
                buildingModel.setObject(null);

                target.add(building);

                ApartmentGroup.this.onChange(target);
            }
        };
        add(apartment);
    }

    protected void onChange(AjaxRequestTarget target){

    }
}
