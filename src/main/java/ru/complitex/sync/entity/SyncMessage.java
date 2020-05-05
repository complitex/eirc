package ru.complitex.sync.entity;

/**
 * @author inheaven on 15.07.2015 18:40.
 */
public class SyncMessage {
    private int entityId;
    private String parentName;
    private Long count;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
