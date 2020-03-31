package ru.complitex.eirc.page.resource;

import org.apache.wicket.Application;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.Arrays;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 11.04.2018 16:26
 */
public class EircJsResourceReference extends JavaScriptResourceReference {
    public static final EircJsResourceReference INSTANCE = new EircJsResourceReference();

    private EircJsResourceReference() {
        super(EircJsResourceReference.class, "js/eirc.js");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        return Arrays.asList(JavaScriptHeaderItem.forReference(Application.get().getJavaScriptLibrarySettings().getJQueryReference()),
                JavaScriptHeaderItem.forReference(MenuJsResourceReference.INSTANCE));
    }
}
