package ru.complitex.eirc.entity;

import ru.complitex.domain.entity.NameDomain;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:36
 */
public class Contract extends NameDomain<Contract> {
    public final static int ID = 17;
    public final static String ENTITY = "contract";

    public final static int NAME = 1;


    public Contract() {
        super(ID, ENTITY, NAME);
    }
}
