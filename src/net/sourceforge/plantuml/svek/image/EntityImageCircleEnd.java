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
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class EntityImageCircleEnd extends AbstractEntityImage {

	private static final int SIZE = 20;
	private final ColorParam param;

	public StyleSignature getDefaultStyleDefinitionCircle() {
		return StyleSignature.of(SName.root, SName.element, SName.activityDiagram, SName.circle);
	}

	public EntityImageCircleEnd(ILeaf entity, ISkinParam skinParam, ColorParam param) {
		super(entity, skinParam);
		this.param = param;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	final public void drawU(UGraphic ug) {
		final UEllipse circle = new UEllipse(SIZE, SIZE);
		double shadowing = 0;
		if (getSkinParam().shadowing(getEntity().getStereotype())) {
			shadowing = 3;
		}
		HColor color = SkinParamUtils.getColor(getSkinParam(), getStereo(), param);
		if (SkinParam.USE_STYLES()) {
			final Style style = getDefaultStyleDefinitionCircle().getMergedStyle(
					getSkinParam().getCurrentStyleBuilder());
			color = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
			shadowing = style.value(PName.Shadowing).asDouble();
		}
		circle.setDeltaShadow(shadowing);
		ug.apply(new UChangeBackColor(null)).apply(new UChangeColor(color)).draw(circle);

		final double delta = 4;
		final UShape circleSmall = new UEllipse(SIZE - delta * 2, SIZE - delta * 2);
		ug.apply(new UChangeBackColor(color)).apply(new UChangeColor(null))
				.apply(new UTranslate(delta + 0.5, delta + 0.5)).draw(circleSmall);
	}

	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}

}
