package ru.complitex.eirc.entity;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 19:37
 */
public class DocumentType extends NameCodeDomain<DocumentType>{
    public final static int ID = 18;
    public final static String ENTITY = "document_type";

    public final static int NAME = 1;
    public final static int CODE = 2;

    public DocumentType() {
        super(ID, ENTITY, NAME, CODE);
    }
}
