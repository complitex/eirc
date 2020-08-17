package ru.complitex.eirc.page;

import ru.complitex.domain.page.DomainPage;
import ru.complitex.eirc.entity.DocumentType;

/**
 * @author Anatoly Ivanov
 * 17.08.2020 20:25
 */
public class DocumentTypePage extends DomainPage<DocumentType> {
    public DocumentTypePage() {
        super(DocumentType.class, DocumentType.NAME);
    }
}
