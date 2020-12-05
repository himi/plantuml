package net.sourceforge.plantuml.eclipse.utils;

/**
 * Extends DiagramTextProvider to support multiple diagram views from single texts
 * @author himi
 *
 */
public interface DiagramTextProviderS3 extends DiagramTextProviderS2 {

    boolean isStyleEnabled(String style);

    void disableStyle(String style);
}
