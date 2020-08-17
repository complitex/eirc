package ru.complitex.eirc.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:43
 */
public abstract class NameCodeDomain<T extends NameCodeDomain<T>> extends NameDomain<T> {
    private final int codeEntityAttributeId;

    public NameCodeDomain(int entityId, String entityName, int nameEntityAttributeId, int codeEntityAttributeId) {
        super(entityId, entityName, nameEntityAttributeId);

        this.codeEntityAttributeId = codeEntityAttributeId;
    }

    public String getCode(){
        return getText(codeEntityAttributeId);
    }

    public void setCode(String code){
        setText(codeEntityAttributeId, code);
    }
}
