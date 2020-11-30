/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 *
 * Original Author:  Arnaud Roques
 * Contribution: Hisashi Miyashita
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.CornerParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.MethodsOrFieldsArea;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockWidth;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.RoundedContainer;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorNone;

public class EntityImageRec extends AbstractEntityImage {
	final private EntityImageClassHeader header;

	final private TextBlock fields;
	final private Url url;
	final private double roundCorner;

	final private static int MIN_WIDTH = 50;
	final private static int MIN_HEIGHT = 40;

	final private LineConfigurable lineConfig;
    final private Dimension2D dimTotal;
    final private HColor borderColor;
    final private HColor backColor;

	public EntityImageRec(IEntity entity, ISkinParam skinParam, PortionShower portionShower) {
        this(entity, skinParam, null, null, null, portionShower);
    }

	public EntityImageRec(IEntity entity, ISkinParam skinParam,
                          Dimension2D dimTotal,
                          HColor borderColor, HColor backColor,
                          PortionShower portionShower) {
		super(entity, skinParam);
        this.dimTotal = dimTotal;
		this.lineConfig = entity;

		HColor borderColor0 = lineConfig.getColors(getSkinParam()).getColor(ColorType.LINE);
		if (borderColor0 == null) {
            borderColor0 = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.classBorder);
		}
        if (borderColor0 != null) {
            this.borderColor = borderColor0;
        } else {
            this.borderColor = borderColor;
        }

        if (backColor == null) {
            backColor = SkinParamUtils.getColor(skinParam, getStereo(), ColorParam.classBackground);
            if (backColor == null) {
                backColor = getEntity().getColors(skinParam).getColor(ColorType.BACK);
            }
        }
        this.backColor = backColor;

		header = new EntityImageClassHeader(entity, getSkinParam(), portionShower);

        final CornerParam cornerParam;
        if (entity.isGroup() && (entity instanceof IGroup)) {
        	IGroup group = (IGroup) entity;
            switch (group.getGroupType()) {
            case STATE:
            case REC_USAGE:
                cornerParam = CornerParam.usage;
                break;
            default:
                cornerParam = CornerParam.DEFAULT;
            	break;
            }
        } else {
            switch (entity.getLeafType()) {
            case USAGE:
            case REC_USAGE:
                cornerParam = CornerParam.usage;
                break;	
            default:
                cornerParam = CornerParam.DEFAULT;
               	break;
            }
        }
        this.roundCorner = getSkinParam().getRoundCorner(cornerParam, null);

		this.url = entity.getUrl99();

		final List<Member> members = entity.getBodier().getFieldsToDisplay();
        Display list = Display.empty();
        for (Member att : members) {
            list = list.addAll(Display.getWithNewlines(att.getDisplay(true)));
        }
        final Stereotype stereotype = entity.getStereotype();
        this.fields = list.create8(new FontConfiguration(getSkinParam(), FontParam.STATE_ATTRIBUTE, stereotype),
                                   HorizontalAlignment.LEFT, skinParam, CreoleMode.FULL, skinParam.wrapWidth());
	}

    private Dimension2D dimFields;

	public Dimension2D calculateDimension(StringBounder stringBounder) {
        this.dimFields = fields.calculateDimension(stringBounder);
        if (dimTotal != null) return dimTotal;
		final Dimension2D dimHeader = header.calculateDimension(stringBounder);
		final Dimension2D dim = Dimension2DDouble.mergeTB(dimHeader, dimFields);
		final Dimension2D result;
		if (dimFields.getHeight() == 0) {
			result = Dimension2DDouble.delta(dim, MARGIN);
		} else {
			result = Dimension2DDouble.delta(dim, MARGIN + MARGIN_LINE);
		}
		return Dimension2DDouble.atLeast(result, MIN_WIDTH, MIN_HEIGHT);
	}

	private void drawInternal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();

		final Dimension2D dimHeader = header.calculateDimension(stringBounder);

		ug = ug.apply(borderColor);

        if (backColor != null) {
            ug = ug.apply(backColor.bg());
        }

		HColor headerBackcolor = getEntity().getColors(getSkinParam()).getColor(ColorType.HEADER);
		if (headerBackcolor == null) {
            headerBackcolor = SkinParamUtils.getColor(getSkinParam(), getStereo(), ColorParam.recHeaderBackground);
		}

		final UStroke stroke = getStroke();
		UGraphic ugHeader = ug;

		final Shadowable rect = new URectangle(widthTotal, heightTotal).rounded(roundCorner)
            .withComment(getEntity().getCodeGetName());
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			rect.setDeltaShadow(4);
		}

        boolean headerLine = true;
		if (roundCorner == 0 && headerBackcolor != null && headerBackcolor.equals(backColor) == false) {
			ug.apply(stroke).draw(rect);
			final Shadowable rect2 = new URectangle(widthTotal, dimHeader.getHeight());
			rect2.setDeltaShadow(0);
			ugHeader = ugHeader.apply(headerBackcolor.bg());
			ugHeader.apply(stroke).draw(rect2);
            headerLine = false;
		} else if (roundCorner != 0 && headerBackcolor != null && headerBackcolor.equals(backColor) == false) {
			ug.apply(stroke).draw(rect);
			final Shadowable rect2 = new URectangle(widthTotal, dimHeader.getHeight()).rounded(roundCorner);
			final URectangle rect3 = new URectangle(widthTotal, roundCorner / 2);
			rect2.setDeltaShadow(0);
			rect3.setDeltaShadow(0);
			ugHeader = ugHeader.apply(headerBackcolor.bg()).apply(headerBackcolor);
			ugHeader.apply(stroke).draw(rect2);
			ugHeader.apply(stroke).apply(UTranslate.dy(dimHeader.getHeight() - rect3.getHeight())).draw(rect3);
			rect.setDeltaShadow(0);
			ug.apply(stroke).apply(new HColorNone().bg()).draw(rect);
		} else {
			ug.apply(stroke).draw(rect);
		}
		header.drawU(ugHeader, widthTotal, dimHeader.getHeight());

        final double xFields = MARGIN;
        final double yFields = dimHeader.getHeight() + MARGIN_LINE / 2;
        if (getEntity().isGroup()) {
            if (dimFields.getHeight() > 0) {
                final double yLine = yFields + dimFields.getHeight() + MARGIN_LINE / 2;
                ug.apply(UTranslate.dy(yLine)).draw(ULine.hline(widthTotal));
            }
        } else if (dimFields.getHeight() == 0) {
            headerLine = false;
        }
        if (headerLine) {
            ug.apply(UTranslate.dy(dimHeader.getHeight())).draw(ULine.hline(widthTotal));
        }
        fields.drawU(ug.apply(new UTranslate(xFields, yFields)));
    }

	final public void drawU(UGraphic ug) {
		ug.startGroup(getEntity().getIdent().toString("."));
		if (url != null) {
			ug.startUrl(url);
		}

		drawInternal(ug);

		if (url != null) {
			ug.closeUrl();
		}
		ug.closeGroup();
	}

	private UStroke getStroke() {
		UStroke stroke = lineConfig.getColors(getSkinParam()).getSpecificLineStroke();
		if (stroke == null) {
			stroke = new UStroke(1.5);
		}
		return stroke;
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

    public static void drawPackageStyle(UGraphic ug,
                                        PackageStyle packageStyle,
                                        IGroup group,
                                        ISkinParam skinParam,
                                        HColor borderColor,
                                        double minX,
                                        double minY,
                                        double maxX,
                                        double maxY,
                                        double xTitle,
                                        double yTitle,
                                        TextBlock ztitle,
                                        TextBlock zstereo) {
        EntityImageRec e = new EntityImageRec(group, skinParam,
                                              new Dimension2DDouble(maxX - minX, maxY - minY),
                                              borderColor,
                                              skinParam.getBackgroundColor(false),
                                              new PortionShower() {
                                                  public boolean showPortion(EntityPortion portion, IEntity entity) {
                                                      return true;
                                                  }
                                              });
        e.drawInternal(ug.apply(new UTranslate(minX, minY)));
    }

	private static TextBlockWidth getTextBlockAttribute(IGroup group, ISkinParam skinParam) {
		final TextBlockWidth attribute;
		final List<Member> members = group.getBodier().getFieldsToDisplay();
		if (members.size() == 0) {
			attribute = new TextBlockEmpty();
		} else {
			attribute = new MethodsOrFieldsArea(members, FontParam.STATE_ATTRIBUTE, skinParam, group.getStereotype(),
					null, SName.stateDiagram);
		}
		return attribute;
	}

    public static void drawPackageStyle0(UGraphic ug,
                                        PackageStyle packageStyle,
                                        IGroup group,
                                        ISkinParam skinParam,
                                        HColor borderColor,
                                        double minX,
                                        double minY,
                                        double maxX,
                                        double maxY,
                                        double xTitle,
                                        double yTitle,
                                        TextBlock ztitle,
                                        TextBlock zstereo) {
        UStroke stroke = group.getColors(skinParam).getSpecificLineStroke();
        if (stroke == null) {
            stroke = new UStroke(1.5);
        }
        if (group.getColors(skinParam).getColor(ColorType.LINE) != null) {
            borderColor = group.getColors(skinParam).getColor(ColorType.LINE);
        }

		final Dimension2D total = new Dimension2DDouble(maxX - minX, maxY - minY);
		final double suppY;
		if (ztitle == null) {
			suppY = 0;
		} else {
			suppY = ztitle.calculateDimension(ug.getStringBounder()).getHeight() + IEntityImage.MARGIN
					+ IEntityImage.MARGIN_LINE;
		}

        Stereotype stereotype = group.getStereotype();
		HColor backColor = group.getColors(skinParam).getColor(ColorType.BACK);
		if (backColor == null) {
            backColor = SkinParamUtils.getColor(skinParam, stereotype, ColorParam.classBackground);
		}
		HColor headerBackcolor = group.getColors(skinParam).getColor(ColorType.HEADER);
		if (headerBackcolor == null) {
            headerBackcolor = skinParam.getHtmlColor(ColorParam.recHeaderBackground, stereotype, false);
            if (headerBackcolor == null) {
            	headerBackcolor = backColor;
            }
		}

		final TextBlockWidth attribute = getTextBlockAttribute(group, skinParam);
		final double attributeHeight = attribute.calculateDimension(ug.getStringBounder()).getHeight();
		final RoundedContainer r = new RoundedContainer(total, suppY,
				attributeHeight + (attributeHeight > 0 ? IEntityImage.MARGIN : 0),
                borderColor, headerBackcolor,
                backColor, stroke);
		r.drawU(ug.apply(new UTranslate(minX, minY)), skinParam.shadowing(group.getStereotype()));

		if (ztitle != null) {
			ztitle.drawU(ug.apply(new UTranslate(xTitle, yTitle)));
		}

		if (attributeHeight > 0) {
			attribute.asTextBlock(total.getWidth()).drawU(
					ug.apply(new UTranslate(minX + IEntityImage.MARGIN, minY + suppY + IEntityImage.MARGIN / 2.0)));
		}

		// final Stereotype stereotype = group.getStereotype();
   }
}
