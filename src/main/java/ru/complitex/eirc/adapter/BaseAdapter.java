package ru.complitex.eirc.adapter;

import ru.complitex.common.mapper.BaseMapper;

/**
 * @author Anatoly Ivanov
 * 21.04.2020 7:36 PM
 */
public abstract class BaseAdapter extends BaseMapper {
    public BaseAdapter() {
        super("remote");
    }
}
