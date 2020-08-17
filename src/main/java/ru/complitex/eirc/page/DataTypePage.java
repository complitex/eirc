package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.DataType;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:27
 */
public class DataTypePage extends DomainPage<DataType> {
    public DataTypePage() {
        super(DataType.class, DataType.NAME);
    }
}
