package ru.complitex.eirc.entity;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:35
 */
public class PaymentParameter extends NameCodeDomain<PaymentParameter>{
    public final static int ID = 13;
    public final static String ENTITY = "payment_parameter";

    public final static int NAME = 1;
    public final static int CODE = 2;

    public PaymentParameter() {
        super(ID, ENTITY, NAME, CODE);
    }
}
