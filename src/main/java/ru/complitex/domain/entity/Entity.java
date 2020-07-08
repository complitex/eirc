package ru.complitex.domain.entity;

import ru.complitex.domain.util.Locales;

import java.io.Serializable;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 30.11.2017 14:27
 */
public class Entity implements Serializable {
    private int id;
    private String name;
    private List<EntityValue> entityValues;

    private List<EntityAttribute> entityAttributes;

    public EntityAttribute getEntityAttribute(int entityAttributesId){
        return entityAttributes.stream().filter(a -> a.getEntityAttributeId() == entityAttributesId).findAny()
                .orElseThrow(() -> new RuntimeException(String.format("EntityAttribute not found by id '%s' for '%s'",
                        entityAttributesId, name)));
    }

    public EntityValue getValue(){
        return entityValues.stream().filter(v -> v.getLocaleId() == Locales.getSystemLocaleId()).findAny().orElse(null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EntityValue> getEntityValues() {
        return entityValues;
    }

    public void setEntityValues(List<EntityValue> entityValues) {
        this.entityValues = entityValues;
    }

    public List<EntityAttribute> getEntityAttributes() {
        return entityAttributes;
    }

    public void setEntityAttributes(List<EntityAttribute> entityAttributes) {
        this.entityAttributes = entityAttributes;
    }
}
