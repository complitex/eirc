package ru.complitex.eirc.entity;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:36
 */
public class ContractType extends NameCodeDomain<ContractType> {
    public final static int ID = 16;
    public final static String ENTITY = "contract_type";

    public final static int NAME = 1;
    public final static int CODE = 2;

    public ContractType() {
        super(ID, ENTITY, NAME, CODE);
    }
}
