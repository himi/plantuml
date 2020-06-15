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
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.AbstractTextBlock;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public final class ConcurrentStateImage extends AbstractTextBlock implements IEntityImage {

	private final List<IEntityImage> inners = new ArrayList<IEntityImage>();
	private final Separator separator;
	private final ISkinParam skinParam;
	private final HColor backColor;

	static enum Separator {
		VERTICAL, HORIZONTAL;

		static Separator fromChar(char sep) {
			if (sep == '|') {
				return VERTICAL;
			}
			if (sep == '-') {
				return HORIZONTAL;
			}
			throw new IllegalArgumentException();
		}

		UTranslate move(Dimension2D dim) {
			if (this == VERTICAL) {
				return UTranslate.dx(dim.getWidth());
			}
			return UTranslate.dy(dim.getHeight());
		}

		Dimension2D add(Dimension2D orig, Dimension2D other) {
			if (this == VERTICAL) {
				return new Dimension2DDouble(orig.getWidth() + other.getWidth(), Math.max(orig.getHeight(),
						other.getHeight()));
			}
			return new Dimension2DDouble(Math.max(orig.getWidth(), other.getWidth()), orig.getHeight()
					+ other.getHeight());
		}

		void drawSeparator(UGraphic ug, Dimension2D dimTotal) {
			final double THICKNESS_BORDER = 1.5;
			final int DASH = 8;
			ug = ug.apply(new UStroke(DASH, 10, THICKNESS_BORDER));
			if (this == VERTICAL) {
				ug.draw(ULine.vline(dimTotal.getHeight() + DASH));
			} else {
				ug.draw(ULine.hline(dimTotal.getWidth() + DASH));
			}

		}
	}

	private HColor getColor(ColorParam colorParam) {
		return new Rose().getHtmlColor(skinParam, colorParam);
	}

	public ConcurrentStateImage(Collection<IEntityImage> images, char concurrentSeparator, ISkinParam skinParam,
			HColor backColor) {
		this.separator = Separator.fromChar(concurrentSeparator);
		this.skinParam = skinParam;
		this.backColor = skinParam.getBackgroundColor();
		this.inners.addAll(images);
	}

	public void drawU(UGraphic ug) {
		System.err.println("drawing " + inners.size());
		final HColor dotColor = getColor(ColorParam.stateBorder);
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		for (int i = 0; i < inners.size(); i++) {
			final IEntityImage inner = inners.get(i);
			inner.drawU(ug);
			final Dimension2D dim = inner.calculateDimension(stringBounder);
			ug = ug.apply(separator.move(dim));
			if (i < inners.size() - 1) {
				separator.drawSeparator(ug.apply(new UChangeColor(dotColor)), dimTotal);
			}
		}

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D result = new Dimension2DDouble(0, 0);
		for (IEntityImage inner : inners) {
			final Dimension2D dim = inner.calculateDimension(stringBounder);
			result = separator.add(result, dim);
		}
		return result;
	}

	public HColor getBackcolor() {
		return backColor;
	}

	public boolean isHidden() {
		return false;
	}

	public Margins getShield(StringBounder stringBounder) {
		return Margins.NONE;
	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}
	
	public double getOverscanX(StringBounder stringBounder) {
		return 0;
	}

}
