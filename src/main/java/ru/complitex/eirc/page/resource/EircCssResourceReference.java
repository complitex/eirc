package ru.complitex.eirc.page.resource;

import de.agilecoders.wicket.core.markup.html.themes.bootstrap.BootstrapCssReference;
import de.agilecoders.wicket.themes.markup.html.material_design.MaterialDesignCssReference;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;

import java.util.Arrays;
import java.util.List;

/**
 * @author Anatoly A. Ivanov
 * 17.03.2020 9:52 PM
 */
public class EircCssResourceReference  extends CssResourceReference {
    public final static EircCssResourceReference INSTANCE = new EircCssResourceReference();

    private EircCssResourceReference() {
        super(EircCssResourceReference.class, "css/eirc.css");
    }

    @Override
    public List<HeaderItem> getDependencies() {
        return Arrays.asList(CssReferenceHeaderItem.forReference(BootstrapCssReference.instance()),
                CssReferenceHeaderItem.forReference(new MaterialDesignCssReference()));
    }
}
