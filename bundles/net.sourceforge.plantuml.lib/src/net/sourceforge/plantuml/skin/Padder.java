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
package net.sourceforge.plantuml.skin;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class Padder {

	private final double margin;
	private final double padding;
	private final HColor backgroundColor;
	private final HColor borderColor;
	private final double roundCorner;

	public static final Padder NONE = new Padder(0, 0, null, null, 0);

	@Override
	public String toString() {
		return "" + margin + "/" + padding + "/" + borderColor + "/" + backgroundColor;
	}

	private Padder(double margin, double padding, HColor backgroundColor, HColor borderColor,
			double roundCorner) {
		this.margin = margin;
		this.padding = padding;
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.roundCorner = roundCorner;
	}

	public Padder withMargin(double margin) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withPadding(double padding) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withBackgroundColor(HColor backgroundColor) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withBorderColor(HColor borderColor) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public Padder withRoundCorner(double roundCorner) {
		return new Padder(margin, padding, backgroundColor, borderColor, roundCorner);
	}

	public final double getMargin() {
		return margin;
	}

	public final double getPadding() {
		return padding;
	}

	public final HColor getBackgroundColor() {
		return backgroundColor;
	}

	public final HColor getBorderColor() {
		return borderColor;
	}

	public TextBlock apply(final TextBlock orig) {
		if (this == NONE) {
			return orig;
		}
		return new AbstractTextBlock() {
			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return Dimension2DDouble.delta(orig.calculateDimension(stringBounder), 2 * (margin + padding));
			}

			public void drawU(UGraphic ug) {
				ug = ug.apply(new UTranslate(margin, margin));
				final UGraphic ug2 = ug.apply(new UChangeBackColor(backgroundColor))
						.apply(new UChangeColor(borderColor));
				final Dimension2D originalDim = orig.calculateDimension(ug.getStringBounder());
				final URectangle rect = new URectangle(Dimension2DDouble.delta(originalDim, 2 * padding))
						.rounded(roundCorner);
				ug2.draw(rect);
				orig.drawU(ug.apply(new UTranslate(padding, padding)));
			}
		};
	}
}