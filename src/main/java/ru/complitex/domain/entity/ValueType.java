package ru.complitex.domain.entity;

import ru.complitex.common.entity.IdEnum;

import java.util.Arrays;
import java.util.Objects;

public enum ValueType implements IdEnum {
    BOOLEAN(1L, "boolean"),
    NUMBER(2L, "number"),
    DECIMAL(3L, "decimal"),
    TEXT(4L, "text"),
    DATE(5L, "date"),
    REFERENCE(6L, "reference"),
    NUMBER_LIST(7L, "number_list"),
    TEXT_LIST(8L, "text_list"),
    REFERENCE_LIST(9L, "reference_list");

    private Long id;
    private String key;

    ValueType(Long id, String key) {
        this.id = id;
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public boolean isSimple(){
        return id < 10;
    }

    public String getKey(){
        return key;
    }

    public static ru.complitex.domain.entity.ValueType getValue(Long id){
        if (id == null){
            return null;
        }

        //noinspection OptionalGetWithoutIsPresent
        return Arrays.stream(values()).filter(t -> Objects.equals(id, t.getId())).findAny().get();
    }
}
