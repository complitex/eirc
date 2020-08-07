package ru.complitex.common.entity;

import ru.complitex.domain.util.Locales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Anatoly A. Ivanov
 * 29.11.2017 16:46
 */
public class Filter<T extends Serializable> implements Serializable {
    public static final String FILTER_EQUAL = "equal";
    public static final String FILTER_LIKE = "like";

    public static final String STATUS_ACTIVE_AND_ARCHIVE = "active_and_archive";

    public static final String OPERATOR_AND = "and";
    public static final String OPERATOR_OR = "or";

    private T object;

    private Long first = 0L;
    private Long count = 0L;

    private Sort sort = new Sort(null, null);

    private boolean ascending = false;

    private String filter = FILTER_LIKE;

    private String status;

    private String operator = OPERATOR_AND;

    private List<Long> entityAttributeIds = new ArrayList<>();

    private int localeId = Locales.getSystemLocaleId();

    private Map<String, Object> map = new HashMap<>();

    public Filter() {
    }

    public Filter(T object) {
        this.object = object;
    }

    public Filter(T object, Long first, Long count) {
        this.object = object;
        this.first = first;
        this.count = count;
    }

    public Filter(Long first, Long count) {
        this.first = first;
        this.count = count;
    }

    public static <T extends Serializable> Filter<T> of(T object){
        return new Filter<>(object);
    }

    public static <T extends Serializable> Filter<T> of(T object, long first, long count){
        return new Filter<>(object, first, count);
    }

    public Filter<T> put(String key, Object value){
        map.put(key, value);

        return this;
    }

    public Filter<T> limit(Long first, Long count){
        this.first = first;
        this.count = count;

        return this;
    }

    public Filter<T> limit(Long count){
        this.first = 0L;
        this.count = count;

        return this;
    }

    public Filter<T> sort(String key, Object value){
        this.sort = new Sort(key, value);

        return this;
    }

    public Filter<T> sort(String key, Object value, boolean ascending){
        this.sort = new Sort(key, value);
        this.ascending = ascending;

        return this;
    }

    public Filter<T> sort(String key, boolean ascending){
        this.sort = new Sort(key);
        this.ascending = ascending;

        return this;
    }

    public String getAsc(){
        return ascending ? "asc" : "desc";
    }

    public String getLimit(){
        return count != null && count > 0 ? " limit " + count + " offset " + first : "";
    }

    public String getOrderLimit(){
        return "order by `" + sort.getKey() + "` " + getAsc() + getLimit();
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Sort getSort() {
        return sort;
    }

    public Filter<T> setSort(Sort sort) {
        this.sort = sort;

        return this;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getFilter() {
        return filter;
    }

    public Filter<T> setFilter(String filter) {
        this.filter = filter;

        return this;
    }

    public String getStatus() {
        return status;
    }

    public Filter<T> setStatus(String status) {
        this.status = status;

        return this;
    }

    public String getOperator() {
        return operator;
    }

    public Filter<T> setOperator(String operator) {
        this.operator = operator;

        return this;
    }

    public List<Long> getEntityAttributeIds() {
        return entityAttributeIds;
    }

    public void setEntityAttributeIds(List<Long> entityAttributeIds) {
        this.entityAttributeIds = entityAttributeIds;
    }

    public int getLocaleId() {
        return localeId;
    }
}
