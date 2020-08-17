package ru.complitex.eirc.entity;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:37
 */
public class DataType extends NameCodeDomain<DataType>{
    public final static int ID = 19;
    public final static String ENTITY = "data_type";

    public final static int NAME = 1;
    public final static int CODE = 2;

    public DataType() {
        super(ID, ENTITY, NAME, CODE);
    }
}
