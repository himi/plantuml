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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class USymbolStack extends USymbol {

	@Override
	public SkinParameter getSkinParameter() {
		return SkinParameter.STACK;
	}

	private void drawQueue(UGraphic ug, double width, double height, boolean shadowing, double roundCorner) {
		final double border = 15;
		final URectangle rect = new URectangle(width - 2 * border, height).rounded(roundCorner);
		if (shadowing) {
			rect.setDeltaShadow(3.0);
		}
		ug.apply(new UChangeColor(null)).apply(UTranslate.dx(border)).draw(rect);
		final UPath path = new UPath();
		if (roundCorner == 0) {
			path.moveTo(0, 0);
			path.lineTo(border, 0);
			path.lineTo(border, height);
			path.lineTo(width - border, height);
			path.lineTo(width - border, 0);
			path.lineTo(width, 0);
		} else {
			path.moveTo(0, 0);
			path.lineTo(border - roundCorner / 2, 0);
			path.arcTo(new Point2D.Double(border, roundCorner / 2), roundCorner / 2, 0, 1);
			path.lineTo(border, height - roundCorner / 2);
			path.arcTo(new Point2D.Double(border + roundCorner / 2, height), roundCorner / 2, 0, 0);
			path.lineTo(width - border - roundCorner / 2, height);
			path.arcTo(new Point2D.Double(width - border, height - roundCorner / 2), roundCorner / 2, 0, 0);
			path.lineTo(width - border, roundCorner / 2);
			path.arcTo(new Point2D.Double(width - border + roundCorner / 2, 0), roundCorner / 2, 0, 1);
			path.lineTo(width, 0);
		}
		ug.apply(new UChangeBackColor(null)).draw(path);
	}

	private Margin getMargin() {
		return new Margin(25, 25, 10, 10);
	}

	@Override
	public TextBlock asSmall(TextBlock name, final TextBlock label, final TextBlock stereotype,
			final SymbolContext symbolContext, final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = UGraphicStencil.create(ug, getRectangleStencil(dim), new UStroke());
				ug = symbolContext.apply(ug);
				drawQueue(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner());
				final Margin margin = getMargin();
				final TextBlock tb = TextBlockUtils.mergeTB(stereotype, label, HorizontalAlignment.CENTER);
				tb.drawU(ug.apply(new UTranslate(margin.getX1(), margin.getY1())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				final Dimension2D dimStereo = stereotype.calculateDimension(stringBounder);
				return getMargin().addDimension(Dimension2DDouble.mergeTB(dimStereo, dimLabel));
			}
		};
	}

	@Override
	public TextBlock asBig(final TextBlock title, HorizontalAlignment labelAlignment, final TextBlock stereotype,
			final double width, final double height, final SymbolContext symbolContext,
			final HorizontalAlignment stereoAlignment) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				final Dimension2D dim = calculateDimension(ug.getStringBounder());
				ug = symbolContext.apply(ug);
				drawQueue(ug, dim.getWidth(), dim.getHeight(), symbolContext.isShadowing(),
						symbolContext.getRoundCorner());
				final Dimension2D dimStereo = stereotype.calculateDimension(ug.getStringBounder());
				final double posStereo = (width - dimStereo.getWidth()) / 2;
				stereotype.drawU(ug.apply(new UTranslate(posStereo, 13)));
				final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
				final double posTitle = (width - dimTitle.getWidth()) / 2;
				title.drawU(ug.apply(new UTranslate(posTitle, 13 + dimStereo.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return new Dimension2DDouble(width, height);
			}
		};
	}

	@Override
	public boolean manageHorizontalLine() {
		return true;
	}

}
