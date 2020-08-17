package ru.complitex.eirc.entity;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:33
 */
public class ServiceType extends NameCodeDomain<ServiceType> {
    public final static int ID = 12;
    public final static String ENTITY = "service_type";

    public final static int NAME = 1;
    public final static int CODE = 2;

    public ServiceType() {
        super(ID, ENTITY, NAME, CODE);
    }
}
