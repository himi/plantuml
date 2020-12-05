package net.sourceforge.plantuml.eclipse.utils;

import java.util.Collection;

/**
 * Extends DiagramTextProvider to support multiple diagram views from single texts
 * @author himi
 *
 */
public interface DiagramTextProviderS2 extends DiagramTextProvider {

    Collection<String> getViews();

    void setView(String view);

    Collection<String> getStyles();

    boolean isStyleEnabled(String style);

    void enableStyle(String style);

    void disableStyle(String style);
}
