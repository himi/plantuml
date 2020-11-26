package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.FileFormatOption.IGraphicsFactory;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public interface IUGraphic2Factory extends IGraphicsFactory {
	UGraphic2 createUGraphic(ColorMapper colorMapper,
                             double scale,
                             Dimension2D dim,
                             final HColor suggested,
                             String svgLinkTarget,
                             String hover,
                             long seed,
                             String preserveAspectRatio);
}
