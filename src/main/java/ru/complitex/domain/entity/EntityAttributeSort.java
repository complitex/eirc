package ru.complitex.domain.entity;

import ru.complitex.common.entity.Sort;

/**
 * @author Anatoly Ivanov
 * 13.07.2020 18:55
 */
public class EntityAttributeSort extends Sort {
    public EntityAttributeSort(EntityAttribute entityAttribute) {
        super("entity_attribute", entityAttribute);
    }
}
